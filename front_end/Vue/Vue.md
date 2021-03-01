## 一、概念

### why前端框架

提高开发效率，减少不必要的DOM操作；提高渲染效率；双向数据绑定的概念（只关心数据的业务逻辑，不再关心DOM是如何渲染的）

Vue中的核心概念：不再操作DOM元素

框架和库的区别：框架是一套完整的解决方案，且对项目入侵性较大；库仅提供某一个小功能，对项目入侵性较少，若某库无法满足需求，则可以切换到其他库实现需求。 



后端中的MVC和前端中的MVVM之间的区别：

- MVC是后端开发的概念；
- MVVM是前端视图层的概念，分为三部分：Model、View、ViewModel。M：保存的是页面中的数据；V：HTML结构；VM：调度者，分割了M和V，每当V层想要获取保存数据的时候，都要由VM做中间的处理。数据双向绑定，是由VM提供的。



## 二、基本代码

在VM实例中如果想要获取data上的数据或者访问methods里的方法，需要带上this；

若要在局部方法中调用data或者methods，需要使用箭头函数(es6)



#### v-clock, v-text

刷新的时候会隐藏插值表达式{{msg}}

```html
<head>
  <style>
  	[v-clock]{
      display: none;
    }
  </style>
</head>

<body>
	<div id="app">
    <p v-clock>
      {{ msg }}	
    </p>
    
    <h4 v-text="msg"></h4>
    <div v-html ="msg2"></div>
  </div>
  
  <script>
  	var vm = new Vue({
      el: '#app',
      data: {
				msg : '123',
      	msg2 : '<h1>alskndlaksnd</h1>',
      }
    })
  </script>
</body>
```

区别：

1. 默认v-text没有闪烁问题
2. v-clock前后的内容不会被隐藏，而v-text会覆盖标签中原本的内容
3. v-clock和v-text填充后的字符串都不会被转义，需要使用v-html



#### v-bind

```html
<body>
  <!--v-bind: 在Vue中，提供的用于绑定属性的指令，可简写为: -->
  <!-- v-bind中可以写合法的js表达式 -->
  <input type="button" value="按钮" :title="mytitle">
  <input type="button" value="按钮" v-bind:title="mytitle">
	<script>
  	var vm = new Vue({
      mytitle: "这是一个自定义title",
    })
  </script>
</body>
```

可被简写为`:`



#### v-on

```html
<body>
  <!-- v-on: 事件绑定机制 -->
  <input type="button" value="按钮" :title="mytitle" v-on:click="alert('hello')">
	<script>
  	var vm = new Vue({
      mytitle: "这是一个自定义title",
    })
  </script>
</body>
```

可被简写为`@`



此外还有mouseover等其他事件

```html
<template>
  <div>
      <input type="button" value="button" :title="mytitle" @mouseover="show">
  </div>
</template>

<script>
export default {
  name: 'std01',
  data () {
    return {
      mytitle: 'this is my title',
    }
  },
  methods: {
      show : function(){
          alert('lxw')
      }
  }
}
</script>
```



#### 事件修饰符

- stop

```html
<template>
  <div @click="divHandler">
    <!-- .stop 阻止冒泡 -->
    <input value="WHAT" type="button" @click.stop="btnHandler">
  </div>
</template>

<script>
export default {
//事件修饰符
    data(){
        return{

        }
    },
    methods:{
        divHandler(){
            console.log('div')
        },
        btnHandler(){
            console.log('btn')
        }
    }
}
</script>
```



- prevent：阻止默认事件的发生（如<a>标签的超链接转跳）
- capture：优先处理
- self：组织自身响应，不组织其向上传递
- once：自己只发生一次



#### v-model

双向数据绑定

v-bind只能实现单向数据绑定，从Model绑定到View。

```html
<template>
  <div @click="divHandler">
    <!-- v-model只能只使用在表单元素中 -->
    <!-- input(radio, text, address, email...) select checkbox textarea 这几个需用v-model实现双向绑定-->
    <input type="text" :value="user" style="width:100%;">
    <p>{{user}}</p>
  </div>
</template>

<script>
export default {
//事件修饰符
    data(){
        return{
            user: 'lixiuwen',
            pswd: '123456',
        }
    }
}
</script>
```

#### 使用样式

1. 使用`:class`

```html
<template>
  <div>
    <!-- 在为class使用v-bind绑定对象时，对象的属性是类名；对象的属性可带引号，也可不带-->
    <h1 :class="['red', 'thin', {active: flag}]">This is my style class used by Vue</h1>
  </div>
</template>

<script>
export default {
//事件修饰符
    data(){
        return{
            flag : true,
        }
    },
}
</script>

<style>
    .red{
        color: red;
    }
    .thin{
        font-weight: 200;
    }
    .italic{
        font-style: italic;
    }
    .active{
        letter-spacing: 0.5em;
    }
</style>
```

2. 使用内联样式

```html
<h1 :style="{color:'red', 'font-size': 200}">这是一个标题01</h1>

<h1 :style="style01">这是一个标题02</h1>

<h1 :style="[style01, style02]">这是一个标题03</h1>

<script>
export default{
	data(){
		return{
      style01:{
				color:'red', 'font-size': 200
      },
      style02:{
				font-style: italic
      }
    }
  }
}
</script>
```



#### v-for

```html
<template>
  <div>
        <p v-for="(item,i) in list" :key="i">索引值: {{i}} id: {{item.id}} --- {{item.name}}</p>
        <p v-for="(val, key) in user" :key="key"> key: {{key}} val:{{val}}</p>
  </div>
</template>

<script>
export default {
    name:'std03',

    data(){
        return{
            list:[
                {id:1, name: 'java'},
                {id:2, name: 'c++'},
                {id:3, name: 'python'},
            ],
            user:{
                name: 'tony',
                age: 21,
                gender: 'male',
            }
        }
    }
}
</script>
```

##### v-key

2.2.0+版本里，当在组件中使用v-for时，key是必需的。

若使用`v-for`出问题，则在:key中使用唯一关键字；关键字需要是数字或字符串



### 三、概念

#### 生命周期

- beforeCreat
- created
- BeforeMount
- Mounted
- beforeUpdate
- updated
- beforeDestroy
- destroy

