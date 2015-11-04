# Shop

Yo Leute, ich bin mal so frei, um euch hier kurz zu erklären, wie man das Projekt und Git aufsetzt. Dazu gibt es ein paar Teilschritte:<br>

<h3>1) Git, SourceTree installieren</h3>
1.1) Als erstes müsst ihr euch einen GitHub-Account anlegen -> Das solltet ihr allein hinkriegen^^<br>
1.2) Als nächstes Git installieren (https://git-scm.com/) BZW. im Ordner C:\Zimmermann ist der Git-Ordner ja schon vorhanden<br>
1.3) Falls noch nicht getan: Umgebungsvariable GIT_HOME anlegen und auf Git-Verzeichnis verweisen; Path-Variable nicht vergessen<br>
1.4) SourceTree (Geile GUI für Git Commands) laden -> https://www.sourcetreeapp.com/<br>

<h3>2) Git-Projekt mit SourceTree klonen</h3>
2.1) SourceTree öffnen (am besten auf Englisch stellen, die deutschen Übersetzungen stören)<br>
2.2) Oben links auf Clone/New -> Clone Repository<br>
2.3) Als SourcePath den Pfad von diesem Repository einfügen (https://github.com/SilverJan/Shop.git)<br>
2.4) Destination Path: Das Verzeichnis, in dem das Projekt am Ende abliegt<br>
2.5) Bookmark this repository ankreuzen -> Clone!<br>

Jetzt lädt Git das Projekt runter und ihr solltet am Ende die Dateien im Zielordner haben.<br>
Es fehlen nun folgende Ordner bzw. Dateien (mittels .gitignore sind die festgelegt):
- build\
- .gradle
- .nb-gradle
- gradle.properties // Diese Datei könnt ihr aus den ursprünglichen Zimmermann-Dateien ziehen, imo braucht man die nicht, außer Gradle-Proxy-Settings, wenn wir in der HS im Proxy das Projekt bauen

Diese Dateien werden mit den folgenden Befehlen angelegt:<br>

<h3>3) Mit Gradle aktuellen Stand bauen</h3>

2.1) Im Verzeichnis das cmd öffnen<br>
2.2) gradlew clean // Gradle löscht mit diesem Befehl potentiell vorhandene Unterordner siehe gradle.build<br>
2.3) gradlew compileTestJava // Gradle zieht mit diesem Befehl die Dependencies und erstellt die .jars<br>


<h3>Weiter...</h3>

Nun könnt ihr das Projekt in NetBeans einbinden (das Verzeichnis) und abgehn.<br>

Damit wir hier zusammen gut arbeiten können, ist es wichtig, dass jeder weiß und versteht, wie Git funktioniert und wie man es bedient. Ich hab mir das mit folgendem Git-Tutorial beigebracht: https://git-scm.com/doc<br>

Bin selber kein krasser Experte, aber die Basics hab ich drauf. Also ruhig fragen, wenns was gibt.

