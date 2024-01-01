package minesweeper
import kotlin.random.Random

fun initField(x: Int, y:Int):MutableList<MutableList<Char>>{
    val f=mutableListOf<MutableList<Char>>()
    repeat(x){
        val col=mutableListOf<Char>()
        repeat(y){
            col.add('.')
        }
        f.add(col)
    }
    return(f)
}

fun printField(f:MutableList<MutableList<Char>>){
    println(" │123456789│")
    println("—│—————————│")
    for (i in 0..f.size-1) {
        println("${i+1}|${f[i].joinToString(separator = "")}|")
    }
    println("—│—————————│")
}
fun printLast(f:MutableList<MutableList<Char>>,m:MutableList<MutableList<Char>>){
    println(" │123456789│")
    println("—│—————————│")
    for (i in 0..f.size-1) {
        print("${i+1}|")
        for (j in 0..f[i].size-1)
            if (m[i][j]=='X') print('X') else print(f[i][j])
        println("|")
    }
    println("—│—————————│")
}
fun putMines(f:MutableList<MutableList<Char>>,minesCount: Int){
    var putMines=0
    while (putMines<minesCount){
        val x=Random.nextInt(f.size)
        val y=Random.nextInt(f[x].size)
        if (f[x][y]=='.') {
            f[x][y]='X'
            putMines++
        }
    }

}
fun lookAround(f:MutableList<MutableList<Char>>,m:MutableList<MutableList<Char>>){
    var cnt=0
    for (x in 0.. m.size-1)
    {
        for (y in 0..m[x].size-1)
        {
            if (m[x][y]=='.')
            {
                if (0<x && 0<y &&                  m[x-1][y-1]=='X') cnt++
                if (0<x &&                         m[x-1][y]=='X') cnt++
                if (0<x && y<m[x].size-1 &&        m[x-1][y+1]=='X') cnt++

                if (y<m[x].size-1 &&               m[x][y+1]=='X') cnt++
                if (0<y &&                         m[x][y-1]=='X') cnt++

                if (x<m.size-1  && y>0 &&          m[x+1][y-1]=='X') cnt++
                if (x<m.size-1 &&                  m[x+1][y]=='X') cnt++
                if (x<m.size-1 && y<m[x].size-1 && m[x+1][y+1]=='X') cnt++


            }
            if (cnt>0) f[x][y]=cnt.digitToChar()

            cnt=0
        }
    }

}
fun minesCorresponds(f:MutableList<MutableList<Char>>, m:MutableList<MutableList<Char>>):Boolean {
    for (x in 0..f.size - 1) {
        for (y in 0..f[x].size - 1) {
            if (m[x][y] == 'X' && f[x][y]!='*') return false
            if (m[x][y] != 'X' && f[x][y]=='*') return false
        }
    }
    return true
}
fun freeCorresponds(d:MutableList<MutableList<Char>>, m:MutableList<MutableList<Char>>):Boolean {
    for (x in 0..d.size - 1) {
        for (y in 0..d[x].size - 1) {
            if (m[x][y]!='X' && d[x][y]!='/' && !d[x][y].isDigit()) return false
        }
    }
    return true
}
fun free(display:MutableList<MutableList<Char>>, field:MutableList<MutableList<Char>>,x:Int,y:Int){
 if (display[x][y]!='.' && display[x][y]!='*') return
 else if (field[x][y]=='.'){
     display[x][y]='/'
     if (0<x) free(display,field,x-1,y)
     if (0<y) free(display,field,x,y-1)
     if (x<display.size-1) free(display,field,x+1,y)
     if (y<display[x].size-1) free(display,field,x,y+1)
 }
 else display[x][y]=field[x][y]

}
fun main() {

    println("How many mines do you want on the field?")
    val minesCount=readln().toInt();
    val field=initField(9,9)
    val mines=initField(9,9)
    val display=initField(9,9)
    var firstFreed=false
    putMines(mines,minesCount)
/*    mines[5][7]='X'
    lookAround(field,mines)
    printField(field)
    printField(mines)
*/

    printField(display)
    while (!minesCorresponds(display,mines) && !freeCorresponds(display,mines)) {
        println("Set/unset mines marks or claim a cell as free:")

        val (x_, y_, cmd) = readln().split(" ")
        val x = y_.toInt() - 1
        val y = x_.toInt() - 1

        when (cmd) {
            "mine" -> {
                if (display[x][y].isDigit()) println("There is a number here!")
                else {
                    if (display[x][y] == '*') display[x][y] = '.'
                    else if (display[x][y] == '.') display[x][y] = '*'
                    printField(display)
                }
            }

            "free" -> {
                if (mines[x][y]=='X')
                {
                    //if (firstFreed) {
                        printLast(display, mines)
                        println("You stepped on a mine and failed!")
                        return
                    //}
                    printField(display)
                }
                else if (field[x][y].isDigit()) {
                    display[x][y]=field[x][y]
                    printField(display)
                    firstFreed=true
                }
                else if (field[x][y]=='.') {
                    free(display, field,x,y)
                    printField(display)
                    firstFreed=true
                }
                else firstFreed=true

            }

        }
    }
    println("Congratulations! You found all the mines!")
}
