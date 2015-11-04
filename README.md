# Shop
SWArch WS15/16

Yo Leute, ich bin mal so frei, um euch hier kurz zu erklären, wie man das Projekt und Git aufsetzt. Dazu gibt es ein paar Teilschritte:

##########################################
1) Git, SourceTree installieren
##########################################
1.1) Als erstes müsst ihr euch einen GitHub-Account anlegen -> Das solltet ihr allein hinkriegen^^
1.2) Als nächstes Git installieren (https://git-scm.com/) BZW. im Ordner C:\Zimmermann ist der Git-Ordner ja schon vorhanden
1.3) Falls noch nicht getan: Umgebungsvariable GIT_HOME anlegen und auf Git-Verzeichnis verweisen; Path-Variable nicht vergessen
1.4) SourceTree (Geile GUI für Git Commands) laden -> https://www.sourcetreeapp.com/

##########################################
2) Git-Projekt mit SourceTree klonen
##########################################
2.1) SourceTree öffnen (am besten auf Englisch stellen, die deutschen Übersetzungen stören)
2.2) Oben links auf Clone/New -> Clone Repository
2.3) Als SourcePath den Pfad von diesem Repository einfügen (https://github.com/SilverJan/Shop.git)
2.4) Destination Path: Das Verzeichnis, in dem das Projekt am Ende abliegt
2.5) Bookmark this repository ankreuzen -> Clone!

Jetzt lädt Git das Projekt runter und ihr solltet am Ende die Dateien im Zielordner haben.
Es fehlen nun folgende Ordner bzw. Dateien (mittels .gitignore sind die festgelegt):
- build\
- .gradle
- .nb-gradle
- gradle.properties

Diese Dateien werden mit den folgenden Befehlen angelegt:

##########################################
3) Mit Gradle aktuellen Stand bauen
##########################################
2.1) Im Verzeichnis das cmd öffnen
2.2) gradlew clean // Gradle löscht mit diesem Befehl potentiell vorhandene Unterordner siehe gradle.build
2.3) gradlew compileTestJava // Gradle zieht mit diesem Befehl die Dependencies und erstellt die .jars

##########################################
Weiter...
##########################################
Nun könnt ihr das Projekt in NetBeans einbinden (das Verzeichnis) und abgehn.

Damit wir hier zusammen gut arbeiten können, ist es wichtig, dass jeder weiß und versteht, wie Git funktioniert und wie man es bedient. Ich hab mir das mit folgendem Git-Tutorial beigebracht: https://git-scm.com/doc

Bin selber kein krasser Experte, aber die Basics hab ich drauf. Also ruhig fragen, wenns was gibt.

