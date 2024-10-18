# NewClassGenerator - IDEA 智能类生成插件

NewClassGenerator 是一款强大的 IntelliJ IDEA 插件，旨在简化 Java 开发过程中的类生成和搜索。它提供了智能类搜索和新类生成功能，大大提高了开发效率。

## 主要特性

- 🔍 支持模糊匹配的高级类搜索
- 🏗️ 基于现有类快速生成新类
- 🔄 灵活的字段选择和映射
- 📦 智能包选择功能
- 🏷️ Lombok 注解支持
- 💬 自动保留字段注释
- ⚡ 实时类文件更新跟踪

## 使用方法

1. 在编辑器中右键点击，选择 "Generate New Class"
2. 在弹出的对话框中搜索并选择一个基础类
3. 选择要包含的字段
4. 选择是否使用 Lombok
5. 输入新类名
6. 选择目标包
7. 点击 "OK" 生成新类

### 示例

```java
// 原始类
public class UserDTO {
    private Long id;
    private String username;
    private String email;
}

// 使用 Dev Gear 生成的新类
@Data // 如果选择使用 Lombok
public class UserEntity {
    private Long id;
    private String username;
    private String email;
    
    // 如果不使用 Lombok，会自动生成 getter 和 setter 方法
}
```

## 配置说明

### 类搜索
- 支持模糊搜索和精确匹配
- 实时更新搜索结果
- 按包名组织搜索结果

### 新类生成
- 自定义新类名称
- 选择性包含原类字段
- 选择是否使用 Lombok 注解

### 字段映射
- 自动匹配同名字段
- 保留原始字段注释

## 安装方法

1. 打开 IntelliJ IDEA
2. 进入 `File > Settings > Plugins`
3. 点击 "Browse repositories" 按钮
4. 搜索 "NewClassGenerator"
5. 点击 "Install" 按钮
6. 重启 IDEA 以激活插件

## 系统要求

- IntelliJ IDEA 2023.1 或更高版本

## 技术特点

1. 智能类搜索
   - 支持模糊匹配和精确匹配
   - 实时搜索结果更新
   - 按包名组织搜索结果

2. 新类生成
   - 基于选定类智能生成新类结构
   - 支持 Lombok 注解集成
   - 自动处理字段复制和方法生成

3. 代码生成
   - 自动生成标准的 Java 代码
   - 保持代码格式和样式
   - 智能处理字段注释的复制

## 常见问题

**Q: 如何修改默认的类搜索行为？**
A: 在插件设置中可以选择默认使用模糊搜索或精确匹配。

**Q: 生成的新类是否支持自定义修改？**
A: 是的，插件生成标准的 Java 代码，您可以根据需要自由修改生成的代码。

**Q: 支持批量生成多个类吗？**
A: 目前插件支持单个类的生成。批量生成功能正在我们的开发计划中。

## 参与贡献

我们欢迎任何形式的贡献！如果您有任何建议或发现了 bug，请创建一个 issue 或提交一个 pull request。

## 开源协议

本项目采用 MIT 协议开源 - 详见 [LICENSE](LICENSE) 文件

## 联系我们

如有任何问题或建议，请联系 [开发者邮箱]。

感谢您使用！