# MillionHeros
V1思路（已完成）：通过截取指定区域的内容，进行OCR文字识别获取问题与答案，再通过百度搜索问题，得到的结果一一匹配答案出现的次数，命中次数越多则越有可能为正确答案。

V2思路（待开发）：识别出问题之后，直接webview展示百度搜索页，再高亮显示命中最高的答案，提高检索效率。


# Tip
1. OCR使用百度API，导入工程直接查看TODO，在对应位置输入你申请的ID和Secret
2. 申请了ID和Secret之后，先运行AuthService.main()，拿到assessToken更改BaiduOCR中TODO位置的assessToken
3. 可以修改MainActivity中判断是否有悬浮窗权限的代码，以兼容Android5.0系统，MediaProjection截屏功能是5.0以上就能用了。

# ~ 记得star ~