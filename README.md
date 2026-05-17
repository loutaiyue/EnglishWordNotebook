# 📖 英语单词笔记本 (loutaiyue/English-Word-Notebook)

一款简洁、优雅且实用的 Android 英语单词查询与记录应用。随时随地查询生词，并加入收藏夹方便日后复习。

## ✨ 核心功能

*   **🔍 快速翻译**：输入英文单词，精准获取中文释义（接入百度翻译 API）。
*   **❤️ 单词收藏**：点击查询结果旁的“红心”，一键将生词加入“历史收藏”列表。
*   **🕒 历史记录**：自动保存“最近查询”记录，找回刚刚看过的单词不费力。
*   **🌗 深浅色模式**：支持白天（Light）和夜间（Dark）模式无缝切换，保护视力。
*   **🎨 现代 UI 设计**：界面清爽无广告，操作符合直觉，右下角悬浮按钮提供快捷菜单。

## 📱 界面预览

<!-- 请将下面的图片链接替换为你上传到 GitHub 后的真实图片链接 -->
| 浅色模式 (Light Mode) | 深色模式 (Dark Mode) |
| :---: | :---: |
| <img src="https://github.com/user-attachments/assets/d9e4992a-3911-41a4-8822-7bb3e98ac5ea" width="300"/> | <img src="https://github.com/user-attachments/assets/00470552-6e9e-4ec9-bff3-ac99119e851c" width="300"/> |

## 📥 下载与安装

你可以直接下载打包好的 APK 安装包并在 Android 手机上安装使用：

1. 前往本仓库的 [Releases 页面](../../releases)。
2. 下载最新版本的 `app-release.apk`。
3. 在手机上确认允许“安装未知来源应用”即可完成安装。

## 🛠️ 技术说明与本地编译 (仅限开发者)

本项目基于 Android Studio 开发。由于翻译功能使用了**百度翻译开放平台 API**，为了保护我的个人 API 密钥，开源代码中已将密钥移除。

如果你想要克隆代码并在本地编译运行，请按以下步骤操作：

1. 克隆本项目到本地：
   ```bash
   git clone https://github.com/loutaiyue/EnglishWordNotebook.git
   ```
2. 前往 [百度翻译开放平台](https://api.fanyi.baidu.com/) 注册账号并申请你自己的 `APP ID` 和 `密钥`。
3. 在代码中找到对应的 API 配置位置，将你的 `APP ID` 和 `密钥` 填入。
4. 使用 Android Studio 重新 Sync 并编译运行。

## 🙋‍♂️ 关于作者

*   开发环境：Android Studio
*   如果有任何问题或建议，欢迎提交 [Issues](../../issues) 或者直接联系我！
