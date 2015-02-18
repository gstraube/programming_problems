/*
 * https://code.google.com/codejam/contest/351101/dashboard#s=p0
 */

object StoreCredit {

  def readInput(): Seq[Int] = Vector(150, 24, 79, 50, 88, 345, 3)

  def findItems(items: Seq[Int], targetSum: Int): (Int, Int) = {
    val sortedItems = items.sortWith(_ < _)

    var foundSecondItem = false

    var firstItemIndex = Math.floor(sortedItems.size.toDouble / 2.0).toInt
    var firstItem = sortedItems(firstItemIndex)

    var secondItem = firstItem

    while (!foundSecondItem) {

      firstItem = sortedItems(firstItemIndex)
      val difference = targetSum - firstItem

      val (foundItem, item) = findSecondItem(sortedItems, difference)

      foundSecondItem = foundItem
      secondItem = item

      firstItemIndex = firstItemIndex - 1
    }

    (firstItem, secondItem)
  }

  def findSecondItem(items: Seq[Int], difference: Int): (Boolean, Int) = {

    val medianIndex = Math.floor(items.size.toDouble / 2.0).toInt
    val median = items(medianIndex)

    if (items.size == 1) {
      if (median == difference) {
        return (true, median)
      } else {
        return (false, median)
      }
    }

    if (difference == median) {
      return (true, median)
    }

    var startIndex = 0
    var endIndex = items.size - 1

    if (difference < median) {
      endIndex = medianIndex - 1
    } else {
      startIndex = medianIndex + 1
    }

    findSecondItem(items.slice(startIndex, endIndex + 1), difference)
  }

  def main(args: Array[String]) {
    val items = readInput()
    val (firstItem, secondItem) = findItems(items, 200)
    println(firstItem, secondItem)
  }

}
