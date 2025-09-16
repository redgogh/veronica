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
import os
from cmath import phase

import console
from pathlib import Path
from jinja2 import Template

pathname = __file__.replace('\\', '/')
script_name = pathname.split('/')[-1].split('.')[0]

configure = {
    'desc': '代码生成器',
    'sys': 'Windows/Linux/MacOS',
}

def render(pathname: str, vargs):
    path = Path(pathname)

    if not path.exists():
        console.write(f'错误：模板文件不存在：{pathname}', console.RED)

    template = Template(path.read_text(encoding='UTF-8'))

    # 渲染页面
    return template.render(
        package=vargs['package'],
        mapping=vargs['mapping'],
        module=vargs['module']
    )

def render_ctrl(tempdir, moduledir, vars):
    """
    生成 Controller 代码
    """
    pathname = f'{moduledir}/controller'

    # 不存在则创建
    Path(pathname).mkdir(parents=True, exist_ok=True)

    rendered = render(f'{tempdir}/Controller.temp', vars)

    with open(f'{pathname}/{vars["module"]["entity"]}Controller.java', 'w', encoding='UTF-8') as javafile:
        javafile.write(rendered)

def render_service(tempdir, moduledir, vars):
    """
    生成 Service 代码
    """
    pathname = f'{moduledir}/service'

    # 不存在则创建
    Path(pathname).mkdir(parents=True, exist_ok=True)

    # Service 接口
    rendered = render(f'{tempdir}/Service.temp', vars)
    with open(f'{pathname}/{vars["module"]["entity"]}Service.java', 'w', encoding='UTF-8') as javafile:
        javafile.write(rendered)

    # Service Impl 实现
    rendered = render(f'{tempdir}/ServiceImplements.temp', vars)
    with open(f'{pathname}/{vars["module"]["entity"]}ServiceImplements.java', 'w', encoding='UTF-8') as javafile:
        javafile.write(rendered)

def render_entities(tempdir, moduledir, vars):
    """
    生成 Entities 代码
    """
    pathname = f'{moduledir}/entities'

    Path(pathname).mkdir(parents=True, exist_ok=True)

    rendered = render(f'{tempdir}/Entity.temp', vars)
    with open(f'{pathname}/{vars["module"]["entity"]}.java', 'w', encoding='UTF-8') as javafile:
        javafile.write(rendered)

def render_mappers(tempdir, moduledir, vars):
    """
    生成 Mapper 代码
    """
    pathname = f'{moduledir}/mappers'

    Path(pathname).mkdir(parents=True, exist_ok=True)

    rendered = render(f'{tempdir}/Mapper.temp', vars)
    with open(f'{pathname}/{vars["module"]["entity"]}Mapper.java', 'w', encoding='UTF-8') as javafile:
        javafile.write(rendered)

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

    parser.add_argument('-P', '--package', required=True, help='包名')
    parser.add_argument('-M', '--module-name', required=True, help='模块名称，如：UserInfo')
    parser.add_argument('-D', '--module-desc', required=True, help='模块描述，如：用户信息')

def handle(args):
    """
    处理命令行参数并执行相应操作。

    该函数接收解析后的命令行参数作为参数，根据参数内容执行特定的业务逻辑。
    通常用于在命令被调用后，根据用户输入的参数执行相应的功能。

    :param args: argparse 模块解析后的参数对象，包含用户输入的参数及选项。
    """
    tempdir = f'{Path(__file__).parents[1]}/templates'
    sourcedir = f'{Path.cwd()}/src/main/java'
    moduledir = f'{sourcedir}/{args.package.replace(".", "/")}'

    mapping = args.package.split('.')[-1]

    vars = {
        'package': args.package,
        'mapping': mapping,
        'module': {
            'varname': args.module_name[0].lower() + args.module_name[1:],
            'entity': args.module_name,
            'desc': args.module_desc,
        }
    }

    render_ctrl(tempdir, moduledir, vars)
    render_service(tempdir, moduledir, vars)
    render_entities(tempdir, moduledir, vars)
    render_mappers(tempdir, moduledir, vars)