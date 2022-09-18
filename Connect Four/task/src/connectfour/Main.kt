package connectfour
import java.lang.NumberFormatException
import java.util.Scanner

data class gameConfig(var name1: String, var name2: String, var rows: Int, var columns: Int,
                      var gameStateList: MutableList<MutableList<Int>>, var numberOfGames: Int,
                      var FPScore: Int = 0, var SPScore: Int = 0)

class Animal(val name: String) {
    init {
        check(name.isBlank()) { "Name must not be blank" }
    }
}

fun initialyze():gameConfig{
    var rows=0
    var columns=0
    val scan=Scanner(System.`in`)
    val string="[' '\t]*[0-9]+[' '\t]*[xX][' '\t]*[0-9]+[' '\t]*"
    val reg=string.toRegex()
    val string1="[0-9]+"
    val reg2=string1.toRegex()
    println("Connect Four")
    println("First player's name:")
    val name1=readln()
    println("Second player's name:")
    val name2=readln()
    while (!(rows in 5..9 && columns in 5..9)){
        println("Set the board dimensions (Rows x Columns)")
        println("Press Enter for default (6 x 7)")
        var str= readln()
        if(str==""){
            rows=6
            columns=7
            break
        }
        if(!reg.matches(str)){
            println("invalid input")
            continue
        }
        str=str.lowercase()
        rows= (reg2.find(str)?.value)!!.toInt()
        str =str.substringAfter('x')
        columns=(reg2.find(str)?.value)!!.toInt()
        if(!(rows in 5..9 && columns in 5..9)) {
            if(!(rows in 5..9)) println("Board rows should be from 5 to 9")
            else println("Board columns should be from 5 to 9")
            continue
        }
        else {
          //  println("$name1 VS $name2")
           // println("$rows X $columns board")
            break
        }
    }
    var gameStateList = mutableListOf(mutableListOf<Int>())
    for (i in 1..rows){
        gameStateList.add(mutableListOf<Int>())
    }
    for(j in 0 until rows){
        for(i in 0 until columns){
            gameStateList[j].add(0)
        }
    }
    var t:Int=1
    while(true){
        println("Do you want to play single or multiple games?")
        println("For a single game, input 1 or press Enter")
        println("Input a number of games:")
        var key=readln()
        if(key=="") break
        try{
            t=key.toInt()
            if(t > 0) break
            else {
                println("Invalid input")
                continue
            }
        }
        catch (e: NumberFormatException){
            println("Invalid input")
        }
    }
    println("$name1 VS $name2")
    println("$rows X $columns board")
    if(t==1) println("Single game")
    else println("Total $t games")
    return gameConfig(name1,name2, rows, columns, gameStateList,t)
}

fun drawBoard(gConf: gameConfig){
    var i:Int
    var j:Int
    for(i in 1..gConf.columns){
        print(" $i")
    }
    println()
    for (i in 0..gConf.rows){
        for(j in 0..gConf.columns){
            if(i==gConf.rows){
                if(j==0) {
                    print("╚")
                    print("═")
                }
                else
                if(j==gConf.columns) println("╝")
                else {
                    print("╩═")
                }
            }
            else{
                if(j==gConf.columns) println("║")
                else {
                    print("║")
                    if(gConf.gameStateList[i][j]==1) print("o")
                    else if(gConf.gameStateList[i][j]==2) print("*")
                    else print(" ")
                }
            }
        }
    }
}

fun checkBoard(gConf: gameConfig, i:Int, j:Int, whosturn: Int):Int{
    var counter=0
    for(k in 0 until gConf.rows){
        if(gConf.gameStateList[k][j]==whosturn) counter++
        else counter=0
        if (counter==4) return 1
    }
    counter=0
    for(k in 0 until gConf.columns){
        if(gConf.gameStateList[i][k]==whosturn) counter++
        else counter=0
        if (counter==4) return 1
    }
    counter=0
    var row=i
    var column=j
    while(row in 0 until gConf.rows && column in 0 until gConf.columns){
        row++
        column++
    }
    row--
    column--
    while(row in 0 until gConf.rows && column in 0 until gConf.columns){
        if(gConf.gameStateList[row][column]==whosturn) counter++
        else counter=0
        if (counter==4) return 1
        row--
        column--
        //println("counter1=$counter")
    }
    counter=0
    row=i
    column=j
    while(row in 0 until gConf.rows && column in 0 until gConf.columns){
        row--
        column++
    }
    row++
    column--
    while(row in 0 until gConf.rows && column in 0 until gConf.columns){
        if(gConf.gameStateList[row][column]==whosturn) counter++
        else counter=0
        if (counter==4) return 1
        row++
        column--
        //println("counter2=$counter")
    }
    for(k in 0 until gConf.columns){
        if(gConf.gameStateList[0][k]!=0) {
            if (k==gConf.columns-1) return 2
            continue
        }
        else break
    }
    return 3
}

fun turn(gConf: gameConfig, whosturn:Int):Boolean{
    var ifGameIsOver:Int
        loop@ while(true) {
            if(whosturn==1) println("${gConf.name1}\'s turn:")
            else println("${gConf.name2}\'s turn:")
            val string="[' '\t]*[0-9]+[' '\t]*"
            val reg=string.toRegex()
            var t=readln()
            if(t=="end") return false
            if(reg.matches(t)){
                val string1="[0-9]+"
                val reg2=string1.toRegex()
                var key=(reg2.find(t)?.value)!!.toInt()
                if(key !in 1..gConf.columns){
                    println("The column number is out of range (1 - ${gConf.columns})")
                    continue
                }
                var checker=0
                for(i in gConf.rows-1 downTo 0){
                    if(gConf.gameStateList[i][key-1]!=0) continue
                    else{
                        gConf.gameStateList[i][key-1]=whosturn
                        ifGameIsOver= checkBoard(gConf,i,key-1,whosturn)
                        if(ifGameIsOver!=3) break@loop
                        checker=1
                        break
                    }
                }
                if (checker==0) println("Column $key is full")
                else return true
            }
            else{
                println("Incorrect column number")
                continue
            }
        }
    if (ifGameIsOver==1){
        drawBoard(gConf)
        if(whosturn==1) {
            gConf.FPScore+=2
            println("Player ${gConf.name1} won")
        }
        else {
            gConf.SPScore+=2
            println("Player ${gConf.name2} won")
        }
    }
    else {
        gConf.FPScore+=1
        gConf.SPScore+=1
        drawBoard(gConf)
        println("It is a draw")
    }
    //println("Game Over!")
    return false
}

fun gameProcess(gConf: gameConfig, i:Int){
    var whosturn=if(i%2==1) 1 else 2
    var ifRunning=true
    drawBoard(gConf)
    while (ifRunning){
        ifRunning=turn(gConf,whosturn)
        if(ifRunning) drawBoard(gConf)
        else {
            if(i==gConf.numberOfGames) {
                if(gConf.numberOfGames>1){
                    println("Score")
                    println("${gConf.name1}: ${gConf.FPScore} ${gConf.name2}: ${gConf.SPScore}")
                }
                println("Game over!")
            }
            else {
                println("Score")
                println("${gConf.name1}: ${gConf.FPScore} ${gConf.name2}: ${gConf.SPScore}")
                for(i in 0 until gConf.columns){
                    for(j in 0 until  gConf.rows){
                        gConf.gameStateList[i][j]=0
                    }
                }
            }
        }
        whosturn = if(whosturn==1) 2 else 1
    }
}



fun main() {
    var gConf = initialyze()
    for(i in 1..gConf.numberOfGames){
        if(gConf.numberOfGames>1){
            println("Game #$i")
        }
        gameProcess(gConf,i)
    }


    //print(rows)
    //print(columns)
}