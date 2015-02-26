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

    var firstItemIndex = Math.floor(sortedItems.size.toDouble / 2.0).toInt + 1
    var firstItem = sortedItems(firstItemIndex)

    var secondItem = -1

    while (secondItem == -1) {

      firstItem = sortedItems(firstItemIndex)
      val difference = targetSum - firstItem
      firstItemIndex = firstItemIndex - 1

      secondItem = findSecondItem(sortedItems, difference)
    }

    (firstItem, secondItem)
  }

  def findSecondItem(items: Seq[Int], difference: Int): Int = {

    if (items.size == 0) {
      return -1
    }

    val medianIndex = Math.floor(items.size.toDouble / 2.0).toInt
    val median = items(medianIndex)

    if (median == difference) {
      return median
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
