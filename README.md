# Super Mario Vector Pathing
This assignment is about finding the shortest path - in terms of moves - from a starting position to an ending.

The board "mario" has to move is a double string array made up of O's, S's, F's, and whitespace.
Mario can move on any S, F or whitespace, but cannot end up out of bounds, or on an O.

# Solution
This assignment is solved by building a tree of all possible mario movements,
and then doing a BFS on this resulting in a set of movements that is the shortest path
from any S to any F
