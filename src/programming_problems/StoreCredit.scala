import scala.io.Source._

/*
 * https://code.google.com/codejam/contest/351101/dashboard#s=p0
 */

object StoreCredit {

  def readInput(fileName: String): Seq[(Int, Seq[Int])] = {
    val lines = fromFile(fileName).getLines()

    val numberOfTestCases = lines.next().toInt

    var testCases = scala.collection.mutable.MutableList[(Int, Seq[Int])]()

    for (i <- 0 to numberOfTestCases - 1) {
      val storeCredit = lines.next().toInt
      val numberOfItems = lines.next().toInt

      var remainingItemsToBeRead = numberOfItems
      while (remainingItemsToBeRead != 0) {
        val items = lines.next().split("\\s+").map(_.toInt)
        testCases += Tuple2(storeCredit, items)
        remainingItemsToBeRead = remainingItemsToBeRead - items.size
      }

    }

    testCases
  }

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
    val testCases = readInput(args(0))
    for ((storeCredit, items) <- testCases) {
      val (firstItem, secondItem) = findItems(items, storeCredit)
      println(firstItem, secondItem)
    }
  }

}
