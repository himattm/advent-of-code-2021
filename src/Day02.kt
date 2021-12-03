/**
--- Day 2: Dive! ---
Now, you need to figure out how to pilot this thing.

It seems like the submarine can take a series of commands like forward 1, down 2, or up 3:

- forward X increases the horizontal position by X units.
- down X increases the depth by X units.
- up X decreases the depth by X units.

Note that since you're on a submarine, down and up affect your depth, and so they have the opposite
result of what you might expect.

The submarine seems to already have a planned course (your puzzle input). You should probably figure
out where it's going. For example:

- forward 5
- down 5
- forward 8
- up 3
- down 8
- forward 2

Your horizontal position and depth both start at 0. The steps above would then modify them as
follows:

- forward 5 adds 5 to your horizontal position, a total of 5.
- down 5 adds 5 to your depth, resulting in a value of 5.
- forward 8 adds 8 to your horizontal position, a total of 13.
- up 3 decreases your depth by 3, resulting in a value of 2.
- down 8 adds 8 to your depth, resulting in a value of 10.
- forward 2 adds 2 to your horizontal position, a total of 15.

After following these instructions, you would have a horizontal position of 15 and a depth of 10.
(Multiplying these together produces 150.)

Calculate the horizontal position and depth you would have after following the planned course.
What do you get if you multiply your final horizontal position by your final depth?

--- Part Two ---
Based on your calculations, the planned course doesn't seem to make any sense. You find the
submarine manual and discover that the process is actually slightly more complicated.

In addition to horizontal position and depth, you'll also need to track a third value, aim, which
also starts at 0. The commands also mean something entirely different than you first thought:

- down X increases your aim by X units.
- up X decreases your aim by X units.
- forward X does two things:
   - It increases your horizontal position by X units.
   - It increases your depth by your aim multiplied by X.

Again note that since you're on a submarine, down and up do the opposite of what you might expect:
"down" means aiming in the positive direction.

Now, the above example does something different:

- forward 5 adds 5 to your horizontal position, a total of 5.
  Because your aim is 0, your depth does not change.
- down 5 adds 5 to your aim, resulting in a value of 5.
- forward 8 adds 8 to your horizontal position, a total of 13.
  Because your aim is 5, your depth increases by 8*5=40.
- up 3 decreases your aim by 3, resulting in a value of 2.
- down 8 adds 8 to your aim, resulting in a value of 10.
- forward 2 adds 2 to your horizontal position, a total of 15.
  Because your aim is 10, your depth increases by 2*10=20 to a total of 60.

After following these new instructions, you would have a horizontal position of 15 and a depth
of 60. (Multiplying these produces 900.)

Using this new interpretation of the commands, calculate the horizontal position and depth you would
have after following the planned course. What do you get if you multiply your final horizontal
position by your final depth?
*/

private fun associateDirections(input: List<String>): Map<String, Int> {
  val result = mutableMapOf<String, Int>()

  input
    .map { it.split(" ") } // Map to a List<List<String>> each element is guaranteed 2 items
    .associateByTo(
      destination = result, // We need a result map to store the current values for reference
      keySelector = { pair -> pair[0] }, // The key will be the direction string
      valueTransform = { pair ->
        // The value is the current direction's amount plus all previous amounts for that direction
        // Default to 0 if the direction doesn't yet exist.
        pair[1].toInt() + result.getOrDefault(pair[0], 0)
      }
    )

  return result
}

const val FORWARD = "forward"
const val DOWN = "down"
const val UP = "up"

fun main() {
  fun part1(input: List<String>): Int {
    val directionsMap = associateDirections(input)

    val forward = directionsMap.getOrDefault(FORWARD, 0)
    val up = directionsMap.getOrDefault(UP, 0)
    val down = directionsMap.getOrDefault(DOWN, 0)

    val depth = down - up // up moves us in the negative direction from the prompt

    return forward * depth
  }

  fun part2(input: List<String>): Int {
    // Start by defining all of the values we will need
    var forward = 0
    var aim = 0
    var depth = 0

    input
      .map { it.split(" ") } // Split up the input into List<List<String>>
      .map { Pair(it[0], it[1].toInt()) } // Make them Pair<String, Int>
      .forEach { pair ->
        // Perform the operations for each direction.
        when (pair.first) {
          FORWARD -> {
            forward += pair.second
            depth += pair.second * aim
          }
          DOWN -> {
            aim += pair.second
          }
          UP -> {
            aim -= pair.second
          }
        }
      }

    return forward * depth
  }

  val input = readInput("Day02")

  // check(part1(input) == 1475)
  println(part1(input))

  // check(part2(input) == 1516)
  println(part2(input))
}
