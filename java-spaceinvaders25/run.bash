for d in ui collider controller  gameobjects playground  ui rendering ; do rm src/spaceinvadersProject/${d}/*.class; rm ../javashooter-framework25/src/javashooter/${d}/*.class ; done
BASE_PROJECT=../javashooter-framework25
javac -cp ./src:$BASE_PROJECT/src -sourcepath src:$BASE_PROJECT/src  ./src/spaceinvadersProject/SpaceInvadersGame.java
echo Compiled!
java -cp ./src:../javashooter-framework25/src spaceinvadersProject.SpaceInvadersGame

