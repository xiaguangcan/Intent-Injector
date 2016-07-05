# Intent-Injector
编译期注解 Intent注入框架

  //使用注解，注解中的传入的字符串就是Intent中的key\n
  @Parameter("ID")
  String id;
  
  //现在只支持Activity，可在activity中绑定框架
  XgcIntentInjector.injector({Activity});
  
  就ok了
  
