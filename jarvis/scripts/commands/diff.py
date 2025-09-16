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
import console
import base64

pathname = __file__.replace('\\', '/')
script_name = pathname.split('/')[-1].split('.')[0]

configure = {
    'desc': '获取两个集合的差集',
    'sys': 'Windows/Linux/MacOS',
}

def reg(subparsers):
    """
    注册命令的处理函数。

    该函数接收一个子解析器作为参数，用于在命令行工具中注册子命令及其相关选项。
    通常在此函数中会定义具体的子命令及其参数，便于后续的命令解析和处理。

    :param subparsers: argparse 模块创建的子解析器对象，用于添加子命令。
    """
    parser = subparsers.add_parser(script_name, help=f"{configure['desc']} ({configure['sys']})")
    parser.add_argument('-a', type=str, help='集合 A')
    parser.add_argument('-b', type=str, help='集合 B')

def handle(args):
    """
    处理命令行参数并执行相应操作。

    该函数接收解析后的命令行参数作为参数，根据参数内容执行特定的业务逻辑。
    通常用于在命令被调用后，根据用户输入的参数执行相应的功能。

    :param args: argparse 模块解析后的参数对象，包含用户输入的参数及选项。
    """
    a_lines = args.a.splitlines()
    b_lines = args.b.splitlines()

    unique_lines = list(set(a_lines) - set(b_lines))

    console.write('\nArguments line count', color=console.RED)
    console.write(f'  -a: {len(a_lines)}', color=console.RED)
    console.write(f'  -b: {len(b_lines)}', color=console.RED)

    unique_lines_count = len(unique_lines)
    console.write(f'list diff count: {unique_lines_count}', color=console.RED)

    if unique_lines_count > 0:
        console.write('>>>', color=console.RED)
    for item in unique_lines:
        console.write(item)

    if unique_lines_count > 0:
        console.write('<<<', color=console.RED)