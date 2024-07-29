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

