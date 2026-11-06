# sandbox.py
import os
from dotenv import load_dotenv
import subprocess
from uuid import uuid4

load_dotenv()
groq_api_key = os.environ["GROQ_API_KEY"]

# helper
def split(cmd):
    return cmd.split(" ")

def create_sandbox(repo_name, clone_url):
    sandbox_id = uuid4()
    sandbox_name = f"sandbox-{sandbox_id.hex}"

    # create sandbox
    subprocess.run(
        split(f"sbx create --name {sandbox_name} opencode"),
        check=True
    )

    # run sandbox
    subprocess.run(
        ["sbx", "-e", 'OPENCODE_CONFIG_CONTENT={"autoupdate":false}', "run", sandbox_name],
        check=True
    )

    # set GROQ_API_KEY
    subprocess.run(
        split(f"sbx secret set groq --sandbox {sandbox_name}"),
        input=groq_api_key + "\n",
        text=True,
        check=True
    )

    # clone repo
    subprocess.run(
        split(f"sbx exec {sandbox_name} git clone {clone_url} {repo_name}"),
        check=True
    )


