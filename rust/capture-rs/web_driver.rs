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
use fantoccini::{Client, ClientBuilder, Locator};
use fantoccini::elements::Element;
use tokio;

const CHROME_DRIVER: &str = "http://127.0.0.1:9515";

pub struct WebClient {
    client: Client
}

impl WebClient {
    fn new(client: Client) -> Self {
        Self { client }
    }

    pub async fn is_bottom(&self) -> bool {
        let ret = self.client.execute(r#"
            return window.scrollY + window.innerHeight >= document.body.scrollHeight;
        "#, vec![]).await;

        ret.unwrap().as_bool().unwrap()
    }

    pub async fn scroll(&self, px: i64) {
        self.client.execute(r#"
            window.scrollBy(arguments[0], window.innerHeight);
        "#, vec![px.into()]).await.expect("[WebClient] 客户端脚本执行失败");
    }

    pub async fn click(&self, element: &Element) -> Result<(), Box<dyn std::error::Error>> {
        element.click().await?;
        Ok(())
    }

    pub async fn find(&self, xpath: &str) -> Option<Element> {
        self.client.find(Locator::XPath(xpath)).await.ok()
    }

    pub async fn send_text(&self, element: &Element, value: &str) -> Result<(), Box<dyn std::error::Error>> {
        element.send_keys(value).await?;
        Ok(())
    }

    pub async fn close(self) {
        self.client.close().await.expect("[WebClient] 客户端关闭失败");
    }

}

pub async fn open_web_client(url: &str) -> Result<WebClient, Box<dyn std::error::Error>>{
    let client = ClientBuilder::native().connect(CHROME_DRIVER).await?;
    client.goto(url).await?;
    Ok(WebClient::new(client))
}