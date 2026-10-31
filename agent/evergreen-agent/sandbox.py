# sandbox.py

import subprocess

# helper
def split(cmd):
    return cmd.split(" ")

def create_sandbox(owner, repo_name, clone_url):
    cmd = "sbx create --name evergreen-demo codex"
    subprocess.run(split(cmd))
