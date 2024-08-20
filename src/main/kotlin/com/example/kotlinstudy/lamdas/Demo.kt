package com.example.kotlinstudy.lamdas

class Demo {

    fun walk1To(n: Int, action: (Int) -> Unit) =
        (1..n).forEach { action(it) }


    fun test(){
        walk1To(5, {i -> print(i)})

        // 콤마 제거, 괄호 생략 가능
        walk1To(5) {i -> print(i)}

        // 암시적 변수 it 사용 가능
        walk1To(5) { print(it) }

        // 함수 참조
        walk1To(5, ::print)
    }


    fun predicateOfLength(length:Int) :(String)->Boolean {
        return { input: String -> input.length == length } // 함수 리턴
    }

    fun predicateOfLength2(length:Int) =
        { input: String -> input.length == length }


    fun  test2(){
        val names = listOf("Pam", "Pat", "Paul", "Paula")
        print(names.find{ name -> name.length == 5 })

        val checkLength = {name:String -> name.length == 5 }
        names.find(checkLength)

        // 람다가 리턴하는 타입이 변수에 지정해놓은 타입과 다르면 컴파일 에러 발생
        val checkLength2: (String) -> Boolean = { name:String -> name.length == 5 }

        // 익명 함수
        names.find(fun (name:String):Boolean { return name.length == 5 })

        names.find(predicateOfLength2(5))

    }

    fun invokeWith(n:Int, action: (Int) -> Unit) {
        println("enter invokeWith $n")
        action(n)
        println("exit invokeWith $n")
    }

    fun caller(){
        (1..3).forEach { i ->
            invokeWith(i) {
                println("enter for $it")
                if (it == 2) {
                    return@invokeWith
                }
                println("exit for $it")
            }
        }
    println("end of caller")
    }

    fun test3() {
        caller()
        println("after return from caller")
    }

    fun test4() {

        val format = "%-10s%-10s%-10s%-10s"
        val str = "context"
        val result = "RESULT"

        fun toString() = "lexical"
        println(String.format("%-10s%-10s%-10s%-10s%-10s%",
            "Method", "Argument", "Reciver", "Return", "Result"))
        println("===============================================")

        // let() 메소드는 컨텍스트 객체(let()을 호출한 객체)를 아규먼트로 람다에게 전달
        val result1 = str.let { arg ->
            print(String.format(format, "let", arg, this, result))
            result
        }
        println(String.format("%-10s", result1))

        // alse() 메소드는 람다의 결과를 무시하고 컨텍스트 객체를 result로 리턴
        val result2 = str.also { arg ->
            print(String.format(format, "also", arg, this, result))
            result
        }
        println(String.format("%-10s", result2))

        val result3 = str.run {
            print(String.format(format, "run", "N/A", this, result))
            result
        }
        println(String.format("%-10s", result3))

        val result4 = str.apply {
            print(String.format(format, "apply", "N/A", this, result))
            result
        }
        println(String.format("%-10s", result4))
    }




}
