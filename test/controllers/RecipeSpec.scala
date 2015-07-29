package controllers

import client.RecipeHttpClient
import models.Recipe
import org.scalatest.{FlatSpec, Matchers}
/**
  *
 */
class RecipeSpec extends FlatSpec with Matchers {

  "Recipe" should "parse json" in new RecipeController {

    override def httpClient: RecipeHttpClient = ???

    val json = """{
                    "name":"Hot Roast Beef Sandwiches",
                    "ingredients":"12 whole Dinner Rolls Or Small Sandwich Buns (I Used Whole Wheat)\n1 pound Thinly Shaved Roast Beef Or Ham (or Both!)\n1 pound Cheese (Provolone, Swiss, Mozzarella, Even Cheez Whiz!)\n1/4 cup Mayonnaise\n3 Tablespoons Grated Onion (or 1 Tbsp Dried Onion Flakes))\n1 Tablespoon Poppy Seeds\n1 Tablespoon Spicy Mustard\n1 Tablespoon Horseradish Mayo Or Straight Prepared Horseradish\n Dash Of Worcestershire\n Optional Dressing Ingredients: Sriracha, Hot Sauce, Dried Onion Flakes Instead Of Fresh, Garlic Powder, Pepper, Etc.)",
                    "url":"http://thepioneerwoman.com/cooking/2013/03/hot-roast-beef-sandwiches/",
                    "image":"http://static.thepioneerwoman.com/cooking/files/2013/03/sandwiches.jpg",
                    "ts":{
                       "$date":1365276013902
                    },
                    "cookTime":"PT20M",
                    "source":"thepioneerwoman",
                    "recipeYield":"12",
                    "datePublished":"2013-03-13",
                    "prepTime":"PT20M",
                    "description":"When I was growing up, I participated in my Episcopal church's youth group, and I have lots of memories of weekly meetings wh..."
                 }"""

    val recipe: Recipe = parseRow(json).get
    recipe.name shouldBe "Hot Roast Beef Sandwiches"
    recipe.url shouldBe "http://thepioneerwoman.com/cooking/2013/03/hot-roast-beef-sandwiches/"

  }
}
