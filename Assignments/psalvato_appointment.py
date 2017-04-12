

#1026 Assignment 3
#Paul Salvatore 250668447
#April 6, 2016


class Appointment(object):
    """A class that takes in the day, month, year, description, and number of an appointment to create an Appointment object."""

    def __init__(self, day, month, year, desc, apptNum):
        self._day = str(day)
        self._month = str(month)
        self._year = str(year)
        self._desc = desc
        self._apptNum = apptNum


    def getApptNum(self):
        """Returns the appointment number."""
        return self._apptNum


    def getDate(self):
        """Returns the date of the appointment in list format."""
        return [self._day, self._month, self._year]


    def getDesc(self):
        """Returns the description of the appointment."""
        return self._desc


    def occursOn(self, date):
        """Takes in a date of the form day/month/year, returns True if the appointment is on that date, False if it is not."""
        date = date.split('/')
        if date[0] == self._day and date[1] == self._month and date[2] == self._year:
            return True
        else:
            return False


    def save(self, file, toAppend = False):
        raise NotImplementedError


    def __repr__(self):
        """Represents the appointment based on its date and description."""
        return 'Appointment on (' + self._day + '/' + self._month + '/' + self._year + '): ' + self._desc



class Onetime(Appointment):
    """A subclass of appointment which is non-repeating."""

    def __init__(self, day, month, year, desc, apptNum):
        super().__init__(day, month, year, desc, apptNum)


    def save(self, file, toAppend = False):
        """Saves the appointment to the file in the form Type|Day|Month|Year|Description.
        If toAppend, will add the entry to the bottom of the file."""
        toWrite = 'O|' + self._day + '|' + self._month + '|' + self._year + '|' + self._desc + '\n'

        file.write(toWrite)


    def __repr__(self):
        return '%38s' % ('One time appointment on (' + self._day + '/' + self._month + '/' + self._year + '): ') + self._desc



class Daily(Appointment):
    """A subclass of appointment that repeats daily."""

    def __init__(self, day, month, year, desc, apptNum):
        super().__init__(day, month, year, desc, apptNum)


    def occursOn(self, date):
        """Determines if the daily appointment was before the entered date.
        If so, returns True, else returns False."""
        before = False
        apptDate = self.getDate()
        if int(apptDate[2]) < int(date[2]):
            before = True
        elif int(apptDate[2]) == int(date[2]) and int(apptDate[1]) < int(date[1]):
            before = True
        elif int(apptDate[2]) == int(date[2]) and int(apptDate[1]) == int(date[1]) and int(apptDate[0]) <= int(date[0]):
            before = True
        return before


    def save(self, file, toAppend = False):
        """Saves the appointment to the file in the form Type|Day|Month|Year|Description.
        If toAppend, will add the entry to the bottom of the file."""
        toWrite = 'D|' + self._day + '|' + self._month + '|' + self._year + '|' + self._desc + '\n'

        file.write(toWrite)


    def __repr__(self):
        return '%38s' % ('Daily appointment on (' + self._day + '/' + self._month + '/' + self._year + '): ') + self._desc



class Monthly(Appointment):
    """A subclass of appointment that repeats monthly."""

    def __init__(self, day, month, year, desc, apptNum):
        super().__init__(day, month, year, desc, apptNum)


    def occursOn(self, date):
        """Determines if the number of the day of the date given of the form day/month/year is the same as the appointment.
        If so, returns True, else returns False. Checks to ensure the appointment is taken place before the date entered."""

        #Determines if the appointment has happened before the entered date.
        before = False
        apptDate = self.getDate()
        if int(apptDate[2]) < int(date[2]):
            before = True
        elif int(apptDate[2]) == int(date[2]) and int(apptDate[1]) < int(date[1]):
            before = True
        elif int(apptDate[2]) == int(date[2]) and int(apptDate[1]) == int(date[1]) and int(apptDate[0]) <= int(date[0]):
            before = True

        #Determines if the day of the entered date coincides with that of the appointment.
        if date[0] == self._day and before:
            return True
        else:
            return False


    def save(self, file, toAppend = False):
        """Saves the appointment to the file in the form Type|Day|Month|Year|Description.
        If toAppend, will add the entry to the bottom of the file."""
        toWrite = 'M|' + self._day + '|' + self._month + '|' + self._year + '|' + self._desc + '\n'

        file.write(toWrite)


    def __repr__(self):
        return '%38s' % ('Monthly appointment on (' + self._day + '/' + self._month + '/' + self._year + '): ') + self._desc

