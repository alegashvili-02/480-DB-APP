CREATE TABLE Client (
    Email TEXT PRIMARY KEY,
    Name TEXT
);


CREATE TABLE Address (
    City TEXT,
    StreetName TEXT,
    Number INT,
    PRIMARY KEY (City, StreetName, Number)
);


CREATE TABLE ClientAddress (
    Email TEXT,
    City TEXT,
    StreetName TEXT,
    Number INT,
    PRIMARY KEY (Email, City, StreetName, Number),
    FOREIGN KEY (Email) REFERENCES Client(Email),
    FOREIGN KEY (City, StreetName, Number)
        REFERENCES Address(City, StreetName, Number)
);


CREATE TABLE CreditCard (
    CardNumber TEXT PRIMARY KEY,
    Email TEXT NOT NULL,
    City TEXT NOT NULL,
    StreetName TEXT NOT NULL,
    Number INT NOT NULL,
    FOREIGN KEY (Email) REFERENCES Client(Email),
    FOREIGN KEY (City, StreetName, Number)
        REFERENCES Address(City, StreetName, Number)
);


CREATE TABLE Hotel (
    HotelID INT PRIMARY KEY,
    City TEXT,
    StreetName TEXT,
    Number INT,
    Name TEXT,
    FOREIGN KEY (City, StreetName, Number)
        REFERENCES Address(City, StreetName, Number)
);

CREATE TABLE Review (
    ReviewID INT,
    HotelID INT,
    Rating INT,
    Email TEXT,
    Message TEXT,
    PRIMARY KEY (HotelID, ReviewID),
    FOREIGN KEY (HotelID) REFERENCES Hotel(HotelID),
    FOREIGN KEY (Email) REFERENCES Client(Email)
);

CREATE TABLE Room (
    RoomNum INT,
    HotelID INT,
    NumWindows INT,
    YearOfLastRenovation INT,
    AccessType TEXT,
    PRIMARY KEY (HotelID, RoomNum),
    FOREIGN KEY (HotelID) REFERENCES Hotel(HotelID)
);

CREATE TABLE Booking (
    BookingID INT PRIMARY KEY,
    PricePerDay INT,
    RoomNum INT NOT NULL,
    HotelID INT NOT NULL,
    Email TEXT NOT NULL,
    StartDate DATE,
    EndDate DATE,
    FOREIGN KEY (Email) REFERENCES Client(Email),
    FOREIGN KEY (HotelID, RoomNum)
        REFERENCES Room(HotelID, RoomNum)
);

CREATE TABLE Manager (
    SSN INT PRIMARY KEY,
    Name TEXT NOT NULL,
    Email TEXT UNIQUE NOT NULL
);

