# Biblioteka

Napisz aplikacje dla firmy Ksiazkopol do zarzadzania biblioteka.

Podpowiedz: Przy projektowaniu endpointow API pamietaj o zasadach REST. Przy projektowaniu schematu danych pamietaj o spojnosci danych oraz relacji.

## Faza 1 - podstawowa funkcjonalnosc

Zaimplementuj API:

* CRUD ksiazek
* CRUD klientow
* wyszukiwanie ksiazek
  * np. po autorze, roku wydania, wydawnictwie
* wypozyczanie ksiazek przez klientow
  * ksiazka moze byc wypozyczona tylko przez jednego klienta w danym czasie
  * ksiazka powinna byc oddana w ciagu 7 dni
* zwracanie wypozyczonej ksiazki
* lista wypozyczonych ksiazek danego klienta
  * domyslnie sortowane po dacie wypozyczenia rosnaco

## Faza 2

Optymalizacja uzycia:

* Podczas wyszukiwania aplikacja frontendowa wyswietla tylko tytul, autora i rok wydania ksiazki.
* Pozwol, aby uzytkownik API wyszukiwania mogl wskazac, ktore pola z ksiazek powinny zostac zwrocone - zaoszczedzi to ilosc przesylanych danych oraz czas przetwarzania

Zaimplmentuj API pozwalajace na tworzenie serii ksiazek:

* CRUD do serii
* dodanie i usuniecie ksiazki do/z serii
* Ksiazka powinna zwracac informacje o serii
  * jest kilka rozwiazan/podejsc jak to zrobic - kazde ma swoje plusy i minusy, wybor zalezy w duzej mierze od sytuacji.
  * warto byloby tutaj sie chwile zatrzymac, zaprojektowac dwa rozwiazania i wskazac ich zady i walety

Rozbudowa funkcjonalnosci wypozyczania:

* Wprowadz tabele audytowa, ktore bedzie przechowywac historie wypozyczen
  * tabele audytowe przechowuja historie modyfikacji rekordow, lub zdarzen biznesowych
  * pozwolaja na analize danych uzycia aplikacji oraz jej stanu
  * oddzielenie danych archiwalnych od ich aktualnego stanu (tabel na ktorych normalnie pracuja aplikacja) nie obciaza wydajnosciowo normalnych operacji
  * o tym zagadnieniu mozemy sobie po prostu pogadac, to bedzie latwiej jakbys nie rozumiala zamyslu 🙂
  * potrzebne dane: ksiazka, klient, data wypozyczenia, data, kiedy uzytkownik powinien oddac ksiazke, data faktycznego oddania
  * podczas wypozyczania ksiazki przez klienta dodaj wpis do tabeli
  * podczas oddawania ksiazki zmodyfikuj wpis dodajac date faktycznego oddawania
* Dodaj podstawowe API analityczne, ktore uzywajac tabeli audytowej pozowli na:
  * Zwrocenie podsumowania aktualnego stanu:
    * ilosc wszystkich ksiazek
    * ilosc wypozyczonych ksiazek
    * ilosc ksiazek mozliwych do wypozyczenia
    * ilosc ksiazek po terminie oddania
  * Zwrocenie wszystkich klientow, ktorzy aktualnie zalegaja ze zwroceniem ksiazki
  * czy widzisz jakies zagrozenia tego API?

Przedluzenie czasu wypozyczenia:

* klient moze jednorazowo przedluzyc czas aktualnie wypozyczonej ksiazki o 7 dni

## Faza 3 - security

Dodaj obsluge JWT

* JWT to aktualnie standard w zabezpieczeniach komunikacji statelessowej
* Aplikacja powinna posiadac nastepujace role:
  * klient
    * moze przegladac, wypozyczac ksiazki, etc
    * moze edytowac informacje o sobie
    * nie moze zarzadzac ksiazkami i uzytkownikami
    * nie moze widziec informacji o innych uzytkownikach
  * analityk
    * moze przegladac ksiazki
    * nie moze edytowac/wypozyczac ksiazek
    * nie moze przegladac danych uzytkownikow
    * moze wykonywac wszystkie operacje analitycznego API
  * admin
    * moze wszystko 🙂

## Faza 4 - problemy prawdziwego swiata i inne usprawnienia

Rezerwacja wypozyczonej ksiazki

* Dodaj mozliwosc rezerwacji ksiazki, ktora jest aktualnie wypozyczona
* ogranicz mozliwosc rezerwacji jednej ksiazki do trzech - czwarta proba rezerwacji wypozyczonej ksiazki powinna zakonczyc sie bledem
* gdy podczas oddawania ksiazki istnieje rezerwacja na nia to powinna zostac automatycznie wypozyczona klientowi, ktory zarezerwowal ja najwczesniej
* zarezerwowanej ksiazce nie da sie przedluzyc czasu wypozyczenia

Biblioteka moze miec wiecej niz jeden egzemplarz ksiazki

Rozwin API analityczne o dane funkcjonalosci:

* 10 najczesciej wypozyczanych ksiazek
* 10 najczesciej wypozyczanych serii
* Klienci, ktorzy spoznili sie z oddaniem ksiazki wiecej niz 3 razy
* Najczesciej rezerwowane - lista ksiazek i ich liczba rezerwacji - pozwoli na planowanie zakupow
* Mozliwosc wybrania przedzialu czasu (od do) dla powyzszych

Przypomnienie klientowi o terminie oddania ksiazek

* Raz dziennie system powinien wysylac maile do klientow, ktorzy zalegaja z ksiazkami albo termin oddania mija dzisiaj
* mail powinien zawierac liste ksiazek i date do kiedy powinny zostac oddane
* podpowiedz: do wykonania metody zgodnie z harmonogramem uzyj springowego @Scheduled i wyrazenia cron
* podpowiedz: do testowania maili mozesz uzyc np. mailhog, przychodzace maila moze podejrzec w web ui:


mailhog:
container_name: mailhog
hostname: mailhog
image: docker.io/mailhog/mailhog
ports:
# SMTP
- "1025:1025"
# HTTP WebUI
- "8025:8025"
  