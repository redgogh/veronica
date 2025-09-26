from pathlib import Path
from jinja2 import Template

import console

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

def render_ctrl(tempdir, moduledir, variables):
    """
    生成 Controller 代码
    """
    pathname = f'{moduledir}/controller'

    # 不存在则创建
    Path(pathname).mkdir(parents=True, exist_ok=True)

    rendered = render(f'{tempdir}/Controller.temp', variables)

    with open(f'{pathname}/{variables["module"]["entity"]}Controller.java', 'w', encoding='UTF-8') as javafile:
        javafile.write(rendered)

def render_service(tempdir, moduledir, variables):
    """
    生成 Service 代码
    """
    pathname = f'{moduledir}/service'

    # 不存在则创建
    Path(pathname).mkdir(parents=True, exist_ok=True)

    # Service 接口
    rendered = render(f'{tempdir}/Service.temp', variables)
    with open(f'{pathname}/{variables["module"]["entity"]}Service.java', 'w', encoding='UTF-8') as javafile:
        javafile.write(rendered)

    # Service Impl 实现
    rendered = render(f'{tempdir}/ServiceImplements.temp', variables)
    with open(f'{pathname}/{variables["module"]["entity"]}ServiceImplements.java', 'w', encoding='UTF-8') as javafile:
        javafile.write(rendered)

def render_entities(tempdir, moduledir, variables):
    """
    生成 Entities 代码
    """
    pathname = f'{moduledir}/entities'

    Path(pathname).mkdir(parents=True, exist_ok=True)

    rendered = render(f'{tempdir}/Entity.temp', variables)
    with open(f'{pathname}/{variables["module"]["entity"]}.java', 'w', encoding='UTF-8') as javafile:
        javafile.write(rendered)

def render_mappers(tempdir, moduledir, variables):
    """
    生成 Mapper 代码
    """
    pathname = f'{moduledir}/mappers'

    Path(pathname).mkdir(parents=True, exist_ok=True)

    rendered = render(f'{tempdir}/Mapper.temp', variables)
    with open(f'{pathname}/{variables["module"]["entity"]}Mapper.java', 'w', encoding='UTF-8') as javafile:
        javafile.write(rendered)

def execute(alias, args):
    """
    执行代码生成任务
    """
    for (k, v) in alias.items():
        args.package = args.package.replace(f'@{k}', v)

    tempdir = f'{Path(__file__).parents[1]}/templates'
    sourcedir = f'{Path.cwd()}/src/main/java'
    moduledir = f'{sourcedir}/{args.package.replace(".", "/")}'

    mapping = args.package.split('.')[-1]

    variables = {
        'package': args.package,
        'mapping': mapping,
        'module': {
            'varname': args.module_name[0].lower() + args.module_name[1:],
            'entity': args.module_name,
            'table': args.entity_table,
            'desc': args.module_desc,
        }
    }

    if 'c' in args.X:
        render_ctrl(tempdir, moduledir, variables)

    if 's' in args.X:
        render_service(tempdir, moduledir, variables)

    if 'm' in args.X:
        render_entities(tempdir, moduledir, variables)
        render_mappers(tempdir, moduledir, variables)