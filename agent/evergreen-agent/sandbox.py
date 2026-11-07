# sandbox.py
import os
import json
import subprocess
from enum import StrEnum, auto
from uuid import uuid4
from pathlib import Path
from tempfile import NamedTemporaryFile
from dotenv import load_dotenv

load_dotenv(Path(__file__).resolve().parents[2] / ".env")

class Sandbox:
    class Status(StrEnum):
        """ State inspection class for Sandbox class """
        BUILDING = auto()
        CREATED = auto()
        FAILED = auto()
        DELETED = auto()

    # constructor
    def __init__(self, db_uuid: uuid4, owner: str, repo_name: str, clone_url: str):
        # param attrs
        self.uuid = db_uuid
        self.owner = owner
        self.repo_name = repo_name
        self.clone_url = clone_url

        # other attrs
        self.sandbox_name = f"sandbox-{db_uuid.hex}"
        self.status = self.Status.BUILDING
        self.error = None

        # try and create sandbox
        try:
            self._create_sandbox()
        except Exception as e:
            self.status = self.Status.FAILED
            self.error = e
            print(f"""
                Failed to create sandbox\n
                {self.__str__}\n
                Error: {e}
            """)

    def _create_sandbox(self):
        """" Creates sandbox unless it already exists """
        self.status = self.Status.BUILDING
        repo_path = f"/home/agent/workspace/{self.repo_name}"

        # antrophic api key work
        anthropic_api_key = os.environ["ANTHROPIC_API_KEY"]
        base_url = os.environ["ANTHROPIC_BASE_URL"]
        model = "anthropic/claude-haiku-5-5"
        config = {
            "autoupdate": False,
            "model": model,
            "small_model": model,
            "providers": {
                "anthropic": {"settings": {"baseURL": base_url}}
            },
        }

        # set credentials
        subprocess.run(
            self._split(f"sbx secret set anthropic --sandbox {self.sandbox_name}"),
            input=anthropic_api_key + "\n",
            text=True,
            check=True
        )

        # create sandbox
        subprocess.run(
            self._split(f"sbx create --name {self.sandbox_name} opencode"),
            check=True
        )

        # clone repo
        subprocess.run(
            self._split(f"sbx exec {self.sandbox_name} git clone") + [self.clone_url, repo_path],
            check=True
        )

        print("DEBUG: repo cloned", flush=True)

        # run opencode in sandbox
        command = ["sbx", "exec", "-w", repo_path,
                   "-e", "OPENCODE_CONFIG_CONTENT=" + json.dumps(config)]
        agent_command = [self.sandbox_name, "opencode", "run", "--standalone",
                         "--model", model] + [
                "Create hello.md containing a short introduction about you. "
                "Use a file editing tool, then verify the file exists."
            ]
        # Docker's secret proxy only substitutes keys for official endpoints.
        # Use a private, short-lived env file for the configured endpoint instead.
        with NamedTemporaryFile(mode="w", prefix="evergreen-anthropic-") as credentials:
            credentials.write(f"ANTHROPIC_API_KEY={anthropic_api_key}\n")
            credentials.flush()
            subprocess.run(command + ["--env-file", credentials.name] + agent_command, check=True)

        self.status = self.Status.CREATED

    # helper
    def _split(self, cmd: str) -> list[str]:
        """ Splits a string into tokens for subprocess.run() """
        return cmd.split(" ")
    
    def is_created(self):
        """ Checks if the sandbox already exists """
        output = subprocess.run(
            self._split(f"sbx ls -q"),
            check=True
        )

        return self.sandbox_name in output.stdout.splitlines

    def __str__(self):
        return f"""
        {self.sandbox_name}
        Owner: {self.owner}
        Repository name: {self.repo_name}
        Clone URL: {self.clone_url}
        Status: {self.status}
        """

    def delete(self):
        subprocess.run(
            self._split(f"sbx rm {self.sandbox_name}"),
            input="y",
            text=True,
            check=True
        )

        self.status = self.Status.DELETED

if __name__ == "__main__":
    repo_name = "chess-timer"
    clone_url = "https://github.com/Mahasvin24/chess-timer.git"
