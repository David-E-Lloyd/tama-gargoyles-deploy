# Helpers from GPT

## Setup
 psql -h 127.0.0.1 gargoyle_springboot_test < qe-resources/python-seed-files/gargoyle_test_seeds_sql
##

### Summary of Each Constraint Type

| Constraint | Purpose | Example |
|------------|---------|---------|
| `PRIMARY KEY` | Unique identifier for each row | No two gargoyles have same id |
| `NOT NULL` | Field is required | Must provide a name |
| `UNIQUE` | No duplicates allowed | Can't have two gargoyles named "Shale" |
| `CHECK` | Only specific values allowed | Type must be BAD, GOOD, or CHILD |
| `FOREIGN KEY` | Links to another table | user_id must exist in users table |
| `ON DELETE CASCADE` | Auto-delete related rows | Delete user → delete their gargoyles |


### What Each Test Gargoyle Is For

| ID | Name | Hunger | Happiness | Use Case |
|----|------|--------|-----------|----------|
| 1 | HealthyGarg | 100 | 100 | Test feeding/playing when already at max |
| 2 | HungryGarg | 10 | 80 | Test feeding functionality |
| 3 | SadGarg | 85 | 15 | Test play functionality |
| 4 | CriticalGarg | 5 | 5 | Test recovery from critical state |
| 5 | MidRangeGarg | 50 | 50 | Test incremental changes |
| 6 | ZeroGarg | 0 | 0 | Test boundary at minimum |
| 7 | ThresholdGarg | 29 | 70 | Test hunger < 30 accelerated decay rule |
| 8 | AboveThresholdGarg | 31 | 70 | Compare with hunger > 30 (normal decay) |
| 9 | RetiredGarg | 100 | 100 | Test different status |
| 10 | OtherUserGarg | 75 | 75 | Test user filtering (user_id = 2) |
| 11 | BadGarg | 60 | 40 | Test BAD type |
| 12 | BoundaryGarg | 100 | 0 | Test opposite boundary extremes |