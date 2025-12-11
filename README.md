# ☀️ 天气预报 App - 广州天气 (Guangzhou Weather)

## 🌟 项目简介

本项目是基于提供的 UI 设计稿实现的一款简洁、现代的天气预报移动应用程序。它采用 **Kotlin** 语言和 **XML** 布局，遵循传统的 **Activity/Fragment + ViewModel** 架构模式，旨在提供当前城市当日的详细天气信息及未来几天的天气预报。

## ✨ 核心功能

* **当日天气概览:** 显示当前温度、最高/最低温度以及天气状态。
* **日夜详情:** 分别展示白天和夜晚的温度、风力及天气描述。
* **多城市切换:** 底部水平导航栏支持快速切换已收藏的城市数据。
* **未来预报:** 提供未来多日（7日）的天气预报列表，包含日期、星期、天气状态和温度区间。
* **沉浸式 UI:** 采用天空渐变色背景，提供优美的视觉体验。

## 🛠️ 技术栈与架构

本项目采用传统的 Android 开发技术栈和架构，确保应用的稳定性和可维护性。

### 核心技术栈

* **语言:** Kotlin
* **布局:** XML
* **UI 组件:**
    * `ConstraintLayout`: 用于复杂和扁平化的布局结构。
    * `RecyclerView`: 用于实现底部城市列表和垂直的未来预报列表。
    * `Vector Drawable`: 用于所有天气图标和导航图标，以保证清晰度。

### 架构模式 (推荐)

本项目遵循 Google 推荐的 **MVVM (Model-View-ViewModel)** 模式，使用以下组件：

| 组件 | 职责 |
| :--- | :--- |
| **View (Activity/Fragment)** | 负责显示 UI、监听用户交互。 |
| **ViewModel** | 存储和管理 UI 相关数据，应对配置更改。 |
| **Model (Repository & Data Sources)** | 处理数据获取（网络请求、本地缓存）和业务逻辑。 |

### 依赖库 (Dependencies)

* `androidx.core` & `androidx.appcompat`
* `androidx.constraintlayout`
* `androidx.recyclerview`
* `androidx.lifecycle` (ViewModel, LiveData)
* *（若涉及网络请求）* `Retrofit` 或 `Ktor`

## 📂 项目结构

* **.** (项目根目录)
    * `README.md`
    * **app/**
        * **src/main/java/**
            * **com.example.weatherapp/**
                * **activity/**
                    * `MainActivity.kt` # 当日天气主界面
                    * `ForecastActivity.kt` # 未来预报界面 (或 Fragment)
                * **adapter/**
                    * `CityAdapter.kt` # 底部城市列表适配器
                    * `ForecastAdapter.kt` # 未来预报列表适配器
                * **model/**
                    * `WeatherData.kt` # 数据模型 (当日天气, 预报)
                    * `City.kt` # 城市数据模型
                * **viewmodel/**
                    * `WeatherViewModel.kt` # 数据管理
        * **src/main/res/**
            * **drawable/**
                * `gradient_sky.xml` # 垂直渐变背景
                * `ic_...` # 天气图标 Vector Drawables
            * **layout/**
                * `activity_main.xml` # 当日天气布局
                * `activity_forecast.xml` # 未来预报布局
                * `item_city.xml` # 底部城市列表项
                * `item_forecast.xml` # 未来预报列表项
            * **values/**
                * `colors.xml`
                * `themes.xml`

## 🚀 安装与运行

### 环境要求

* Android Studio (Flamingo 或更高版本)
* Android SDK (API 24+)
* Kotlin 1.8+

### 步骤

1.  **克隆仓库**

2.  **导入项目:**
    使用 Android Studio 打开克隆的文件夹。
3.  **同步依赖:**
    等待 Gradle 同步完成。
4.  **运行应用:**
    选择一个模拟器或真机设备，点击运行按钮 (Run 'app')。

## 📸 UI 截图

| 当日天气主界面 | 未来预报列表 |
|![当日天气主界面截图](gradle/screenshots/MainPage.png | ![未来预报列表截图](gradle/screenshots/ForecastPage.png) |
|  |  |
| *显示当前温度、日夜详情和城市列表* | *显示未来多日天气，包括日期、状态和温差* |