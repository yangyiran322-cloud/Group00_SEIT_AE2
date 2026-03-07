# Group00_SEIT_AE2
AE2 小组开发规则（请所有人遵守）

统一环境：Java 17 + Maven。提交 PR 前请先跑：mvn test，必须 BUILD SUCCESS。

包根统一：所有代码都放在 uk.ac.gla.seit.ae2 下面，不要新建其他根包。

主干分支：只认 main。以后开发一律从 main 拉分支，不要在 master 上写。

分支命名规范：feature/<姓名缩写>-<模块>

例：feature/B-ui-menu、feature/E-repo-csv、feature/D-services

提交方式：所有改动都通过 Pull Request 合并到 main（不要直接改 main）。

接口/签名约束：不要随意改 public 类名/方法签名/包名；如果必须改，先在群里确认。

合并顺序建议（减少冲突）：
C(domain) → E(repository实现) → D(service实现) → B(UI菜单) → F(test+截图) → A(最终集成接线)

我的职责（成员A / Tech Lead & Integration）：负责主干集成、接线、合并节奏控制，保证 main 始终可运行。
