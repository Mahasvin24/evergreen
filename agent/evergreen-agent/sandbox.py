# sandbox.py
import os
from pathlib import Path
from dotenv import load_dotenv
import subprocess
from uuid import uuid4

load_dotenv(Path(__file__).resolve().parents[2] / ".env")

# helper
def split(cmd):
    return cmd.split(" ")

def create_sandbox(repo_name, clone_url):
    openai_api_key = os.environ["OPENAI_API_KEY"]
    sandbox_id = uuid4()
    sandbox_name = f"sandbox-{sandbox_id.hex}"
    repo_path = f"/home/agent/workspace/{repo_name}"

    # set credentials
    subprocess.run(
        split(f"sbx secret set openai --sandbox {sandbox_name}"),
        input=openai_api_key + "\n",
        text=True,
        check=True
    )

    # create sandbox
    subprocess.run(
        split(f"sbx create --name {sandbox_name} opencode"),
        check=True
    )

    # clone repo
    subprocess.run(
        split(f"sbx exec {sandbox_name} git clone") + [clone_url, repo_path],
        check=True
    )

    print("DEBUG: repo cloned", flush=True)

    # run opencode in sandbox
    subprocess.run(
        split("sbx exec -w") + [repo_path] + split(
            '-e OPENCODE_CONFIG_CONTENT={"autoupdate":false} '
            f"{sandbox_name} opencode run --standalone "
            "--model openai/gpt-5.4-mini"
        ) + [
            "Create hello.md containing a short introduction about you. "
            "Use a file editing tool, then verify the file exists."
        ],
        check=True,
    )

    return sandbox_name, repo_path

if __name__ == "__main__":
    repo_name = "chess-timer"
    clone_url = "https://github.com/Mahasvin24/chess-timer.git"
    create_sandbox(repo_name, clone_url)
