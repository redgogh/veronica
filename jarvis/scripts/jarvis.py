"""

    Copyright (C) 2019-2024 RedGogh All rights reserved.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

"""
import sys
import argparse
import importlib
import os

parser = argparse.ArgumentParser(prog='Jarvis', description='Jarvis')
subparsers = parser.add_subparsers(dest='command', help='command')

current_dir = os.path.dirname(os.path.abspath(__file__))
commands_dir = os.path.join(current_dir, 'commands')

# import work dir
sys.path.append(commands_dir)

for filename in os.listdir(commands_dir):
    if filename.endswith('.py') and filename != '__init__.py':
        module_name = filename[:-3]
        module = importlib.import_module(f'commands.{module_name}')
        if hasattr(module, 'reg'):
            module.reg(subparsers)

args = parser.parse_args()

module_name = f"commands.{args.command}"

try:
    module = importlib.import_module(module_name)
    module.handle(args)
except ModuleNotFoundError:
    print(f"-error cannot found command: {args.command}")
