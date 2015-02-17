/*
 * https://code.google.com/codejam/contest/351101/dashboard#s=p0
 */

object StoreCredit {

  def readInput(): Seq[Int] = List(150, 24, 79, 50, 88, 345, 3)

  def findItems(items: Seq[Int]): (Int, Int) = {
    val sortedItems = items.sortWith(_<_)

    val firstItemIndex = 0
    val secondItemIndex = 0
    /*
        Extract initial median value

        Calculate difference between target sum and the median

        If the difference is less or equal than the median,
        search recursively in the left half of sortedItems. Otherwise search
        in the right half.

        If a matching item is not found, proceed with the item to the right.

     */

    (firstItemIndex, secondItemIndex)
  }

  def main(args: Array[String]) {
    val items = readInput()
    val (firstItem, secondItem) = findItems(items)
    println(firstItem, secondItem)
  }

}
