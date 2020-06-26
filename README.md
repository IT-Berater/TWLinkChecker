[![Build Status](https://travis-ci.org/IT-Berater/TWLinkChecker.svg?branch=master)](https://travis-ci.org/IT-Berater/TWLinkChecker) 
[![codecov](https://codecov.io/gh/IT-Berater/TWLinkChecker/branch/master/graph/badge.svg)](https://codecov.io/gh/IT-Berater/TWLinkChecker) 
[![Github Releases](https://img.shields.io/github/downloads/atom/atom/latest/total.svg)](https://github.com/IT-Berater/TWLinkChecker)
[![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](https://github.com/IT-Berater/TWLinkChecker)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.wenzlaff.linkchecker/de.wenzlaff.linkchecker/badge.svg)](https://search.maven.org/search?q=de.wenzlaff.linkchecker)

# TWLinkChecker
Excel Link checker

Programm zum checken von URLs in Exceltabellen. Es wird die Syntax und die Erreichbarkeit der URLs überprüft.

Braucht nun mindestens Java 11.

Programmaufruf: de.wenzlaff.linkchecker.CheckExcelUrls (Spalte Nummer von links mit den URLs die überprüft werden soll) (Excel Dateiname)

Aufruf z.B.: de.wenzlaff.linkchecker.CheckExcelUrls 28 exceldatei.xlsx
