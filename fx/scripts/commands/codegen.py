"""

    Copyright (C) 2019-2024 Viakko All rights reserved.

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
import _code_G
import console
import configparser

from pathlib import Path

script_name = __file__.replace('\\', '/').split('/')[-1].split('.')[0]

configure = {
    'desc': '代码生成器',
    'sys': 'Windows/Linux/MacOS',
}

def read_alias_data(pathname):
    config = configparser.ConfigParser()
    config.optionxform = str

    try:
        content_str = ''
        with open(pathname, 'r') as file:
            content_str = '[DEFAULT]\n' + file.read()
        config.read_string(content_str)
        return dict(config['DEFAULT'])
    except Exception as e:
        console.write(f"读取配置文件错误: {e}", color=console.RED)
        return {}

def write_alias_data(data, pathname):
    with open(pathname, 'w', encoding='utf-8') as f:
        for key, value in data.items():
            f.write(f'{key}={value}\n')

def reg(subparsers):
    """
    注册命令的处理函数。

    该函数接收一个子解析器作为参数，用于在命令行工具中注册子命令及其相关选项。
    通常在此函数中会定义具体的子命令及其参数，便于后续的命令解析和处理。

    :param subparsers: argparse 模块创建的子解析器对象，用于添加子命令。
    """
    parser = subparsers.add_parser(script_name, help=f"{configure['desc']} ({configure['sys']})")

    parser.add_argument('--basedir', help='代码所在目录（默认运行目录）')
    parser.add_argument('--tempdir', help='模板目录')
    parser.add_argument('--alias', const=True, nargs='?', help='设置别名，示例：--alias "COM=com.example"')
    parser.add_argument('--delete', action='store_true', help='标识删除操作，--delete --alias "<key>"')

    parser.add_argument('-p', '--package', help='包名')
    parser.add_argument('-t', '--entity-table', help='数据库表名')
    parser.add_argument('-m', '--module-name', help='模块名称，如：UserInfo')
    parser.add_argument('-d', '--module-desc', help='模块描述，如：用户信息')

    parser.add_argument('-X', type=str, help="组合标志：-s -c -m")

def handle(args):
    """
    处理命令行参数并执行相应操作。

    该函数接收解析后的命令行参数作为参数，根据参数内容执行特定的业务逻辑。
    通常用于在命令被调用后，根据用户输入的参数执行相应的功能。

    :param args: argparse 模块解析后的参数对象，包含用户输入的参数及选项。
    """
    alias_data = dict()

    # 生成配置文件路径
    conf_home = f'{Path.home()}/Documents/{script_name}'
    Path(conf_home).mkdir(parents=True, exist_ok=True)
    alias_file = f'{conf_home}/alias.properties'

    # 读取配置
    if Path(alias_file).exists():
        alias_data = read_alias_data(alias_file)

    if args.alias is not None:
        if args.alias is True:
            for (k, v) in alias_data.items():
                print(f' * {k}: {v}')
        else:
            if args.delete:
                del alias_data[args.alias]
            else:
                alias_arr = args.alias.split("=")
                alias_data[alias_arr[0]] = alias_arr[1]

            write_alias_data(alias_data, alias_file)
        exit()

    # 生成代码
    _code_G.execute(alias_data, args)