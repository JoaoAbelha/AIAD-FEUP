# AIAD-FEUP

[Presentation](https://github.com/JoaoAbelha/AIAD-FEUP/blob/main/AIAD_Grupo23_Trabalho2.pdf)

## Multi Agent Simulation

Today, cities are characterized by high traffic density. Is it possible to build a multi-agent system in order to mitigate delays and long waits in transit? Our work will try to answer this question by trying to set up a real scenario, with the following characteristics: 

* Cars are driving on the road and have an origin and a destination. These cars may have different navigation strategies: shortest path, fastest path, path with the fewest roads;

* There are also priority cars, which means that cars driving on a road where a priority car enters will stop until the priority car overtakes them in order to let it pass. In this way they will get to their destination without any problems. These cars will always take the fastest route in order to reach their emergency in the shortest possible time;

* Every road in the city has information about its distance, its maximum speed and its traffic density. These variables will be used to determine the best route for the car;

* Because road safety is important, the maximum speed on each road will be restricted depending on the weather conditions in the city.

### Agents Interaction

![](https://i.imgur.com/4jslQqv.png)

### Simulation Visualization

![](https://i.imgur.com/9W7U5LY.gif)



