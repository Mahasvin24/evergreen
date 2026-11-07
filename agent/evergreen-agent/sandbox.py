# sandbox.py
import os
import subprocess
from enum import StrEnum, auto
from uuid import uuid4
from pathlib import Path
from dotenv import load_dotenv

load_dotenv(Path(__file__).resolve().parents[2] / ".env")

class Sandbox:
    class Status(StrEnum):
        """ State inspection class for Sandbox class """
        BUILDING = auto()
        CREATED = auto()
        FAILED = auto()

    def __init__(self, db_uuid: uuid4, owner: str, repo_name: str, clone_url: str):
        """ Constructor """
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

    # helper
    def _split(self, cmd: str) -> list[str]:
        """ Splits a string into tokens for subprocess.run() """
        return cmd.split(" ")

    def _create_sandbox(self):
        """" Creates sandbox unless it already exists """
        openai_api_key = os.environ["OPENAI_API_KEY"]
        repo_path = f"/home/agent/workspace/{repo_name}"

        # set credentials
        subprocess.run(
            self._split(f"sbx secret set openai --sandbox {sandbox_name}"),
            input=openai_api_key + "\n",
            text=True,
            check=True
        )

        # create sandbox
        subprocess.run(
            self._split(f"sbx create --name {sandbox_name} opencode"),
            check=True
        )

        # clone repo
        subprocess.run(
            self._split(f"sbx exec {sandbox_name} git clone") + [clone_url, repo_path],
            check=True
        )

        print("DEBUG: repo cloned", flush=True)

        # run opencode in sandbox
        subprocess.run(
            self._split("sbx exec -w") + [repo_path] + self._split(
                '-e OPENCODE_CONFIG_CONTENT={"autoupdate":false} '
                f"{sandbox_name} opencode run --standalone "
                "--model openai/gpt-5.4-mini"
            ) + [
                "Create hello.md containing a short introduction about you. "
                "Use a file editing tool, then verify the file exists."
            ],
            check=True,
        )

        self.status = self.Status.CREATED

    


if __name__ == "__main__":
    repo_name = "chess-timer"
    clone_url = "https://github.com/Mahasvin24/chess-timer.git"
