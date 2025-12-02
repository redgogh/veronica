//!
//!    Copyright (C) 2019-2024 Viakko All rights reserved.
//!
//!    Licensed under the Apache License, Version 2.0 (the "License");
//!    you may not use this file except in compliance with the License.
//!    You may obtain a copy of the License at
//!
//!        http://www.apache.org/licenses/LICENSE-2.0
//!
//!    Unless required by applicable law or agreed to in writing, software
//!    distributed under the License is distributed on an "AS IS" BASIS,
//!    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//!    See the License for the specific language governing permissions and
//!    limitations under the License.
//!

use std::time::Duration;
use fantoccini::elements::Element;
use tokio;
mod web_driver;
mod workflows;

async fn run_task(task: &workflows::Task, client: &web_driver::WebClient) {
    let mut curelem: Option<Element> = None;

    for inst in &task.insts {
        if task.sleep > 0 {
            tokio::time::sleep(Duration::from_secs(task.sleep)).await;
        }

        match inst.inst_type {
            workflows::InstType::LOC => {
                let element = client.find(&inst.value).await;
                if let Some(unwrap_element) = element {
                    curelem = Some(unwrap_element);
                }
            },
            workflows::InstType::SEND => {
                match curelem {
                    Some(ref elem) => client.send_text(elem, &inst.value).await
                        .expect("[INST] SEND 指令执行失败"),
                    None => panic!("[WebDriver] 当前元素不可用")
                }
            },
            workflows::InstType::CLICK => {
                match curelem {
                    Some(ref elem) => client.click(elem).await
                        .expect("[INST] CLICK 指令执行失败"),
                    None => panic!("[WebDriver] 当前元素不可用")
                }
            },
            workflows::InstType::LOOP => {},
            workflows::InstType::END => {},
        }
    }
}

#[tokio::main]
async fn main() -> Result<(), Box<dyn std::error::Error>> {
    // load workflows config
    let tasks = workflows::load(&String::from("./workflows.yaml"))?;
    // open web client
    // let client = web_driver::open_web_client("https://tht.changhong.com/#/user/login").await?;
    let client = web_driver::open_web_client("https://github.com/Viakko/devtools").await?;

    for task in tasks {
        if task.enable {
            run_task(&task, &client).await;
        }
    }

    tokio::time::sleep(std::time::Duration::from_secs(5)).await;

    client.close().await;

    Ok(())
}