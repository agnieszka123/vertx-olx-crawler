# vertx-olx-crawler
Olx crawler project written with the use of Vert.x framework

Web Crawler used to search for OLX (online marketplace) offers containing specified keyword.

1. Clone repo from GitHub
2. Make sure you have MVN and JDK installed
2. For Windows run redeploy.bat. For other systems run redeploy.sh.
3. Make a http://localhost:8082/offers/olx:keyword GET request (replace "keyword" with anything you want to search for).

Remarks:

• Keyword needn’t to be a word ;) It can be few strings long (then they should be separated with
the space).
• Keyword can contain polish letters – results of the search will be different for “kon” and “koń”
• When we try to search with unpopular word on OLX, the website will “fix” the parameter and
redirect us to the proper (of course according to OLX) results. In the crawler I enforced
searching for exactly given keyword, as in this case, it seems more logical and I expect that the
user of our crawler knows better than OLX what he wants to search for.
