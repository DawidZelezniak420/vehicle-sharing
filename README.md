# Vehicle Sharing Application

_Aplikacja w trakcie budowy._

## Opis wersji końcowej

Vehicle sharing application jest systemem, który umożliwia użytkownikom wypożyczenie pojazdów takich jak samochody czy motocykle.

### Główne funkcje:
- Użytkownik wybiera przedział czasowy, w którym chce dokonać rezerwacji.
- Zwracane są pojazdy dostępne w danym terminie.
- Klient może wypożyczyć więcej niż jeden pojazd na raz.
- Po wprowadzeniu odpowiednich danych do utworzenia rezerwacji, obliczana jest wstępna kwota (kaucja + cena za wynajem) dla każdego pojazdu.
- Użytkownik dokonuje zapłaty, a jego rezerwacja jest zapisywana w bazie.
- Na 24 godziny przed wypożyczeniem, klient otrzymuje powiadomienie (e-mail oraz SMS) o zbliżającym się wypożyczeniu.
- Klient może zrezygnować z wypożyczenia, a jego opłata zostanie zwrócona na konto.
- W momencie rozpoczęcia wynajmu, rezerwacja użytkownika przekształca się w wypożyczenie.
- Po zwróceniu pojazdu, jeśli zauważone zostaną jakiekolwiek nieprawidłowości, można naliczać dodatkowe opłaty, które zostaną potrącone z kaucji.

## Co aktualnie działa

- Rejestracja i logowanie użytkownika przy użyciu JWT.
- Pobieranie aktywnych w danym przedziale czasowym pojazdów.
- Operacje typu CRUD na pojazdach oraz użytkownikach.
- Ograniczenie dostępu do poszczególnych części aplikacji na podstawie ról użytkowników.
- Szukanie pojazdów po kryteriach takich jak marka, model, rok produkcji, etc.


# Vehicle Sharing Application

_Application under development._

## Final Version Description

Vehicle sharing application is a system that allows users to rent vehicles such as cars or motorcycles.

### Main Features:
- The user selects the time frame in which they want to make a reservation.
- Available vehicles for the selected time frame are returned.
- The client can rent more than one vehicle at a time.
- After entering the necessary data to create a reservation, an initial amount is calculated (deposit + rental price) for each reserved vehicle.
- The user makes a payment and their reservation is saved in the database.
- 24 hours before the rental, the client receives a notification (email and SMS) about the upcoming rental.
- The client can cancel the rental, and their payment will be refunded.
- At the start of the rental, the user's reservation turns into a rental.
- After returning the vehicle, if any irregularities are noticed, additional fees can be charged, which will be deducted from the deposit.

## Current Functionality

- User registration and login using JWT.
- Retrieving active vehicles in a given time frame.
- CRUD operations on vehicles and users.
- Access restriction to specific parts of the application based on user roles.
- Searching vehicles by criteria such as brand, model, year of production, etc.

