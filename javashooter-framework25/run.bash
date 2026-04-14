for d in javashooter.ui javashooter.collider  javashooter.controller  javashooter.gameobjects javashooter.playground  javashooter.ui javashooter.rendering ; do rm src/${d}/*.class; done

javac -cp src src/${1}.java
java -cp src ${1}

