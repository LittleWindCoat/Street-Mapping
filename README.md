# Street Mapping

Introduction
This project will require you to create a rudimentary mapping program in Java. Given a data set representing the roads and intersections in a specific geographic region, your program should be able to plot a map of the data and provide shortest path directions between any two arbitrary intersections using Dijkstra’s algorithm.
 
Deliverable
Basic Mapping
• Implement your own Graph, Node and Edge classes. For this you may use code available online or other sources, but you must cite the source.
• Construct a Graph object using the information read in from the specified input file
• Draw the map using Java Graphics (no third party graphing libraries allowed). The map should scale with the size of the window.
Directions Between Intersections
• Implement Dijkstra’s algorithm to find the shortest path between any two arbitrary intersections, as provided by the command line arguments.
• When the shortest path has been discovered, the intersections followed to reach the destination should be printed out to the console in order. Additionally, your program should print out the total distance traveled in miles.
• Finally, if the program is displaying the map, it should highlight (in a different color, stroke width, etc.) the solution path found.