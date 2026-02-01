# Multi-Objective TSP

A Java implementation of the **Multi-Objective Traveling Salesman Problem (TSP)** using **NSGA-II** (Non-dominated Sorting Genetic Algorithm II). The algorithm optimizes two conflicting objectives: **total distance** and **total travel time**.

## Overview

Given a set of cities and two matrices—one for distances (Euclidean, derived from coordinates) and one for travel times—the solver finds a set of **Pareto-optimal** tours. Each tour represents a trade-off between minimizing distance and minimizing time.

## Features

- **NSGA-II** with fast non-dominated sorting and crowding distance
- **Binary tournament selection** based on dominance
- **Crossover operators**: PMX, OX, CX (Partially Mapped, Order, Cycle)
- **Mutation operators**: Swap, Insert, Invert, Scramble
- Output of the Pareto front to a text file

## Requirements

- Java 8 or higher

## Project Structure

```
src/
├── moop_inclass.java       # Main entry point
├── NSGA_II.java            # NSGA-II algorithm
├── ProblemInstance.java    # Loads TSP instance and matrices
├── Solution.java           # Tour representation and evaluation
├── Population.java         # Population management
├── Dominance.java          # Pareto dominance comparison
├── NonDominatedSorting.java # Fast non-dominated sort + crowding distance
├── NonDominatedPopulation.java
├── Operators.java          # Selection, crossover, mutation
├── CrowdingDistanceComperator.java
├── FitnessComperator.java
├── Heuristics.java
├── SA.java
└── TSP_Time_Matrix.txt     # Sample time matrix (52×52)
```

## Input Files

### 1. TSP Instance (Coordinates)

Place TSP instances in `problem_instances_TSP/` relative to the run directory. Supported format:

```
DIMENSION: 52
NODE_COORD_SECTION
1 565.0 575.0
2 25.0 185.0
...
52 1740.0 245.0
EOF
```

- `DIMENSION:` number of cities
- `NODE_COORD_SECTION` followed by lines: `index x y`
- Ends with `EOF`

The distance matrix is computed internally as Euclidean distances between coordinates.

### 2. Time Matrix

Place time matrices in `time_matrices_TSP/` relative to the run directory. Format: space-separated `n×n` matrix where entry `(i,j)` is travel time from city `i` to city `j`.

Example for 3 cities:
```
0.0 1.5 2.3
1.5 0.0 0.8
2.3 0.8 0.0
```

## Building & Running

### Compile

From the `src` directory:

```bash
cd src
javac *.java
```

### Run

```bash
cd src
java moop_inclass
```

Ensure the following paths exist relative to the working directory (`src/` when run as above):

- `./problem_instances_TSP/berlin52.tsp` (or another TSP file)
- `./time_matrices_TSP/TSP_Time_Matrix.txt`

To use `TSP_Time_Matrix.txt` from `src/`, you can edit `moop_inclass.java` to point to `./TSP_Time_Matrix.txt` and create `./problem_instances_TSP/` with your TSP instance.

### Quick Setup (using existing time matrix)

1. Create `src/problem_instances_TSP/` and add a `.tsp` file (e.g. `berlin52.tsp`)
2. Create `src/time_matrices_TSP/` and copy `TSP_Time_Matrix.txt` there, or update the path in `moop_inclass.java` to `./TSP_Time_Matrix.txt` if it stays in `src/`

## Configuration

In `moop_inclass.java`, NSGA-II is initialized with:

```java
NSGA_II nsga2 = new NSGA_II(popSize, maxGenerations, crossoverRate, mutationRate);
// Default: (100, 100, 0.7, 0.3)
```

- **Population size**: 100  
- **Generations**: 100  
- **Crossover rate**: 0.7 (PMX by default)  
- **Mutation rate**: 0.3 (Swap by default)  

## Output

- **Console**: Pareto front solutions with distance, time, rank, and crowding distance
- **File**: `NSGA-II_Results.txt` with tours and their distance/time values

## Author

Cihanser Çalışkan
