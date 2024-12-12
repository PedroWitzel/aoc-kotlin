import kotlin.time.measureTime

class Day12 {

    class Region(val plant: Char, val border: Position, position: Position, potentialPlants: Set<Position>) {

        val plants = buildSet {
            add(position)
            val neighbors = neighborsOf(position, potentialPlants).toMutableSet()
            while (neighbors.isNotEmpty()) {
                val next = neighbors.first()
                neighbors.remove(next)
                add(next)
                val nextNeighbors = neighborsOf(next, potentialPlants).filter { it !in this }
                neighbors.addAll(nextNeighbors)
            }
        }

        fun neighborsOf(pos: Position, potentialPlants: Set<Position>) =
            pos.neighbors(border).filter { it in potentialPlants }

        fun isInRegion(pos: Position) = pos in plants

        var perimeter: Int = 0
        fun addPlant(pos: Position, map: List<String>) {
            if (pos.x == 0 || map[pos.x - 1][pos.y] != plant) ++perimeter
            if (pos.x == border.x || map[pos.x + 1][pos.y] != plant) ++perimeter
            if (pos.y == 0 || map[pos.x][pos.y - 1] != plant) ++perimeter
            if (pos.y == border.y || map[pos.x][pos.y + 1] != plant) ++perimeter
        }

        fun List<String>.get(pos: Position) =
            if (pos.x in 0..this.size - 1 && pos.y in 0..this.first().length - 1) this[pos.x][pos.y] else null

        fun addPlantWithDiscount(pos: Position, map: List<String>) {
            val up = map.get(pos.up())
            val down = map.get(pos.down())
            val left = map.get(pos.left())
            val right = map.get(pos.right())
            val leftUp = map.get(pos.left().up())
            val leftDown = map.get(pos.left().down())
            val rightUp = map.get(pos.right().up())
            val rightDown = map.get(pos.right().down())

            if (left != plant && up != plant && leftUp != plant) ++perimeter
            if (right != plant && up != plant && rightUp != plant) ++perimeter
            if (left != plant && down != plant && leftDown != plant) ++perimeter
            if (right != plant && down != plant && rightDown != plant) ++perimeter

            if (right == plant && down == plant && rightDown != plant) ++perimeter
            if (right == plant && up == plant && rightUp != plant) ++perimeter
            if (left == plant && down == plant && leftDown != plant) ++perimeter
            if (left == plant && up == plant && leftUp != plant) ++perimeter

            if (left != plant && up != plant && leftUp == plant) ++perimeter
            if (left != plant && down != plant && leftDown == plant) ++perimeter
            if (right != plant && up != plant && rightUp == plant) ++perimeter
            if (right != plant && down != plant && rightDown == plant) ++perimeter
        }

        fun cost() = (plants.size.toLong() * perimeter).also {
            println("A region of $plant plants with price ${plants.size} * $perimeter = $it")
        }

    }

    companion object {
        const val TEST1 = 1930L
        const val TEST2 = 1206L

        fun part1(input: List<String>): Long {
            val border = Position(input.size - 1, input.first().length - 1)
            val groups = mutableListOf<Region>()

            val map =
                input.mapIndexed { i, line -> line.mapIndexed { j, plant -> plant to Position(i, j) } }.asSequence()
                    .flatten().groupBy({ it.first }, { it.second }).map { it.key to it.value.toSet() }
            map.forEach { (plant, positions) ->
                positions.forEach { pos ->
                    var groupToAdd = groups.find { it.isInRegion(pos) }
                    if (groupToAdd == null) {
                        groupToAdd = Region(plant, border, pos, positions)
                        groups.add(groupToAdd)
                    }
                    groupToAdd.addPlant(pos, input)
                }
            }
            return groups.sumOf { it.cost() }
        }

        fun part2(input: List<String>): Long {
            val border = Position(input.size - 1, input.first().length - 1)
            val groups = mutableListOf<Region>()

            val map =
                input.mapIndexed { i, line -> line.mapIndexed { j, plant -> plant to Position(i, j) } }.asSequence()
                    .flatten().groupBy({ it.first }, { it.second }).map { it.key to it.value.toSet() }
            map.forEach { (plant, positions) ->
                positions.forEach { pos ->
                    var groupToAdd = groups.find { it.isInRegion(pos) }
                    if (groupToAdd == null) {
                        groupToAdd = Region(plant, border, pos, positions)
                        groups.add(groupToAdd)
                    }
                    groupToAdd.addPlantWithDiscount(pos, input)
                }
            }
            return groups.sumOf { it.cost() }
        }
    }
}

fun main() {

    val day = "Day12"

    val testInput = readInput("${day}_test")
//    check(Day12.part1(testInput).also(::println) == Day12.TEST1)
    check(Day12.part2(testInput).also(::println) == Day12.TEST2)

    val input = readInput(day)

    measureTime {
        Day12.part1(input).println()
    }.also { println("Part1 took $it") }

    measureTime {
        Day12.part2(input).println()
    }.also { println("Part2 took $it") }
}