//!
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
use std::fs;
use Vec;
use serde::{Deserialize, Deserializer};

macro_rules! instruction {
    ($inst:expr, $prefix:expr, $text:expr) => {
        if $text.starts_with($prefix) {
            return Some(
                Inst::new($inst, $text.replace(&($prefix.to_string() + " "), ""), String::from($text))
            );
        }
    };
}

fn from_enable_bool<'de, D>(deserializer: D) -> Result<bool, D::Error>
    where D: Deserializer<'de>, {
    let value: String = Deserialize::deserialize(deserializer)?;
    match value.to_lowercase().as_str() {
        "on" => Ok(true),
        "off" => Ok(false),
        _ => Err(serde::de::Error::custom(format!("invalid value for {}", value))),
    }
}

#[derive(Debug, Deserialize)]
struct Job {
    pub name: String,
    #[serde(deserialize_with = "from_enable_bool")]
    pub enable: bool,
    pub sleep: u64,
    pub insts: Vec<String>,
}

#[derive(Debug, Deserialize)]
struct Workflows {
    pub name: String,
    pub jobs: Vec<Job>,
}

pub enum InstType {
    LOC,
    SEND,
    CLICK,
    LOOP,
    END,
}

pub struct Inst {
    pub inst_type: InstType,
    pub value: String,
    pub text: String,
}

impl Inst {
    fn new(inst_type: InstType, value: String, text: String) -> Self {
        Self { inst_type, value, text }
    }
}

pub struct Task {
    pub name: String,
    pub enable: bool,
    pub sleep: u64,
    pub insts: Vec<Inst>,
}

impl Task {
    fn new(name: String, enable: bool, sleep: u64) -> Self {
        Self { name, enable, sleep, insts: Vec::new() }
    }
}

fn parse_inst(mut text: String) -> Option<Inst> {
    let text = text.trim();

    instruction!(InstType::LOC, "@loc", text);
    instruction!(InstType::SEND, "@send", text);
    instruction!(InstType::CLICK, "@click", text);
    instruction!(InstType::LOOP, "@loop", text);
    instruction!(InstType::END, "@end", text);

    None
}

pub fn load(path: &String) -> Result<Vec<Task>, Box<dyn std::error::Error>> {
    let yaml_content = fs::read_to_string(path)?;
    let workflows: Workflows = serde_yaml::from_str(&yaml_content).map_err(|e| Box::new(e))?;

    let mut tasks: Vec<Task> = Vec::new();

    for job in workflows.jobs {
        let mut task = Task::new(job.name, job.enable, job.sleep);
        for inst in job.insts {
            match parse_inst(inst) {
                Some(instruction) => task.insts.push(instruction),
                None => return Err(Box::new(std::io::Error::new(std::io::ErrorKind::InvalidData, "invalid instruction"))),
            }
        }
        tasks.push(task);
    }

    Ok(tasks)
}