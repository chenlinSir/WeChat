//app.js
App({
  onLaunch: function () {
    // 展示本地存储能力
    var logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)

    // 登录
    wx.login({
      success: res => {
        // 发送 res.code 到后台换取 openId, sessionKey, unionId
      }
    })
    // 获取用户信息
    wx.getSetting({
      success: res => {
        if (res.authSetting['scope.userInfo']) {
          // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
          wx.getUserInfo({
            success: res => {
              // 可以将 res 发送给后台解码出 unionId
              this.globalData.userInfo = res.userInfo

              // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
              // 所以此处加入 callback 以防止这种情况
              if (this.userInfoReadyCallback) {
                this.userInfoReadyCallback(res)
              }
            }
          })
        }
      }
    }),
    //解决因机型不同导致布局混乱
      wx.getSystemInfo({
        success(res) {
          console.log('设备品牌:', res.brand)
          console.log('设备型号:', res.model)
          console.log('设备像素比:', res.pixelRatio)
          console.log('屏幕宽度:', res.windowWidth)
          console.log('屏幕高度:', res.windowHeight)
          console.log('状态栏的高度:', res.statusBarHeight)
          console.log('微信设置的语言:', res.language)
          console.log('微信版本号:', res.version)
          console.log('操作系统及版本:', res.system)
          console.log('客户端平台:', res.platform)
          console.log('用户字体大小:', res.fontSizeSetting)
          console.log('客户端基础库版本 :', res.SDKVersion)
          console.log('设备性能等级:', res.benchmarkLevel)
        }
      })
  },
  globalData: {
    userInfo: null
  }
})