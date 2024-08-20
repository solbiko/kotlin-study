
## 람다함수

```kotlin
fun walk1To (n:Int, action: (Int)-> Unit) = (1..n).forEach{action(it)}
fun walk1To (n:Int, action: (Int)-> Unit) = (1..n).forEach(action)
```
<br>

```kotlin
walk1To(5, {i -> print(i)})
walk1To(5) {i -> print(i)} // 콤마 제거, 괄호 생략 가능
walk1To(5) { print(it) } // 암시적 변수 it 사용 가능
walk1To(5, ::print) // 함수 참조
```
<br>

```kotlin
val checkLength = {name:String -> name.length == 5 }
val checkLength2: (String) -> Boolean = { name:String -> name.length == 5 }
```
리턴 타입 변수에 지정해놓은 타입과 다르면 컴파일 에러 발생
<br>

### 인라인 함수
- 인라인 함수를 사용하게 되면 코드는 객체를 항상 새로 만드는것이 아니라 해당 함수의 내용을 호출한 함수에 넣는 방식으로 컴파일 코드를 작성하게 됨

```kotlin
inline fun invokeTwo(
    n: Int, 
    action1: (Int) -> Unit, 
    noinline action2: (Int) -> Unit // 최적화에서 제외
) : (Int) -> Unit {
 
    println("enter invokeTwo $n")

    // 두 개의 람다 실행
    action1(n)
    action2(n)

    println("exit invokeTwo $n")

    // 생성한 람다 리턴, 리턴되는 람다는 인풋 파라미터를 무시하고 메시지만  출력
    return { _: Int -> println("lambda returned from invokeTwo") }
}

fun callInvokeTwo() {
    invokeTwo(1, {i -> // 논로컬 리턴, 라벨 리턴 허용
        if (i == 1) { return }
        report(i)
    }, {i -> 
        if (i == 2) { return } // 에러, 라벨 리턴 허용
        report(i)
    })
}
```
- return은 람다에서 기본적으로 허용이 안됨
- 
#### 라벨 리턴
- 라벨 리턴을 사용하면 현재 동작중인 람다를 스킵할 수 있음

#### 논로컬 리턴
- 자신을 둘러싸고 있는 블록보다 더 바깥에 있는 다른 블록을 반환하게 만드는 return문
  - 논로컬 리턴을 사용하면 현재 동작중인 람다를 선언한 곳 바깥으로 나갈 수 있음. 하지만 람다를 받는 함수가 inline으로 선언된 경우만 사용 가능
  - 인라인 람다에 논로컬 리턴을 사용할 수 있음, 인라인을 사용하지 않는 람다는 논로컬 리턴 사용할 수 없음
  - crossinline도 인라인 최적화를 해줌. 람다가 전달된 곳이 아닌 람다가 사용된 곳에서 인라인 최적화 

<br>


## Scope functions

| Method | Argument | Receiver | Result  |
|--------|----------|----------|---------|
| let    | context  | lexical  | RESULT  |
| also   | context  | lexical  | context |
| run    | N/A      | context  | RESULT  |
| apply  | N/A      | context  | context |
<br>

### apply
- fun <T> T.apply(block: T.() -> Unit): T
- 객체 자신을 다시 반환
```kotlin
val adam = Person("Adam").apply {
    age = 32
    city = "London"        
}
println(adam)
```
```kotlin
val mailer = Mailer().apply {
    from("builder@aa.com")
    to("rec@aa.com")
    subject("subject")
    body("details")
}
val result = mailer.send()
print(result)
```


### run
- fun <T, R> T.run(block: T.() -> R): R
- 값을 계산할 때나 여러 개의 지역변수 범위를 제한할 때 사용
```kotlin
val result = service.run {
    port = 8080
    query(prepareRequest() + " to port $port")
}

// the same code written with let() function:
val letResult = service.let {
    it.port = 8080
    it.query(it.prepareRequest() + " to port ${it.port}")
}
```
```kotlin
val mailer = Mailer().run { 
    // 메소드의 연속족인 호출 이후에 인스턴스를 더 이상 쓸 일이 없디면 사용 가능
    from("builder@aa.com")
    to("rec@aa.com")
    subject("subject")
    body("details")
    send() // 람다 결과 리턴
}
print(result)
```


### let
- fun <T, R> T.let(block: (T) -> R): R 
- T?.let { } 형태로 블럭 안을 non-null 인지 확인할 수 있음
```kotlin
val numbers = mutableListOf("one", "two", "three", "four", "five")

numbers.map { it.length }.filter { it > 3 }.let {
    println(it)
}
```
```kotlin
val name = person?.let { it.name } ?: "이름없음"
```
```kotlin
val result = prepareAndSend(createMailer())

val result = createMailer.let { mailer ->
  prepareAndSend(mailer)
}
val result = createMailer.let { it ->
  prepareAndSend(it)
}
// createMailer()의 결과는 let()으로 전달되고, 
// let()은 전달받은 파라미터를 다시 prepareAndSend() 메소드에 전달한다
val result = createMailer.let (::prepareAndSend)
```

     
### also
- fun <T> T.also(block: (T) -> Unit): T
- 객체의 속성을 전혀 사용하지 않거나 변경하지 않고 사용하는 경우
- 객체의 데이터 유효성을 확인하거나, 디버그, 로깅 등의 부가적인 목적으로 사용할 때에 적합
```kotlin
val numbers = mutableListOf("one", "two", "three")
numbers
    .also { println("The list elements before adding new one: $it") }
    .add("four")
```
```kotlin
val mailer = createmailer()
prepareMailer(mailer)
sendMailer(mailer)

createMailer().also(::prepareMailer).also(::sendMailer)
```


### with
- fun <T,R> with(receiver: T, block: T.() -> R): R 
- non-null 객체를 사용하고 블럭의 반환값이 필요하지 않을 때 사용


## DSL
### infix
```kotlin
class Meeting(val title:String) { 
    var startTime: String = ""
    var endTime: String = ""
    private fun convertToString(time: Double) = String.format("$.02f", time)
    fun at (time: Dopble) {startTime = convertToString(time)}
    fun by (time: Dopble) {startTime = convertToString(time)}
    override fun toString() = "$title Meeting starts $startTime ends $endTime"
}

infix fun String.meeting (block: () -> Unit) {
    val meeting = Meeting(this)
    meeting.block()
    println(meeting)
}

"Release Planning" meeting {
    at(14.30)
    by(15.20)
}
```
```kotlin
class Meeting(val title:String) { 
    var startTime: String = ""
    var endTime: String = ""
  
    var start = this
    var end = this

    private fun convertToString(time: Double) = String.format("$.02f", time)
    
    // infix 추가
    infix fun at (time: Dopble) {startTime = convertToString(time)}
    infix fun by (time: Dopble) {startTime = convertToString(time)}
    override fun toString() = "$title Meeting starts $startTime ends $endTime"
}

infix fun String.meeting (block: Metting.() -> Unit) {
    val meeting = Meeting(this)
    meeting.block()
    println(meeting)
}

"Release Planning" meeting {
   start at 14.30 // this.start.at(14.30)
   end by 15.20 // this.end.by(15.20)
}
```
```kotlin

// 클래스 분리: start에서 by를 호출하거나, end에서 at을 호출 할 수 있으므로 
// (start와 end 모두 같은 인스턴스를 리턴하는 속성이고 at과 by 메소드는 Meeting 클래스의 메소드이기 때문
// at과 by 메소드를 분리된 클래스로 옮겨서 이런 잠재적인 에러 예방

open class MeetingTime(var time:String = ""){
    protected fun convertToString(time: Double)= String.format("$.02f", time)
}
class StartTime : MeetingTime(){
  infix fun at (time: Dopble) {startTime = convertToString(time)}
}
class EndTime : MeetingTime(){
  infix fun by (time: Dopble) {startTime = convertToString(time)}
}
  class Meeting(val title:String) {
    var start = StartTime()
    var end = EndTime()
    override fun toString() = "$title Meeting starts $startTime ends $endTime"
}
```

## Java에서 Kotlin 호출
### static
```kotlin
companion object {
    @JvmStatic
    fun create() = Counter(0)
}
```
### 기본 아규먼트로 함수 사용 
```kotlin
@JvmOverloads
fun add (n:Int = 1) = Counter(value+n)
println(counter.add(3))
println(counter.add())
```
```java
System.out.println(counter.add(3));
System.out.println(counter.add()); //error -> @JvmOverloads
```

