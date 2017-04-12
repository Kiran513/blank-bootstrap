

#1026 Assignment 3
#Paul Salvatore 250668447
#April 6, 2016


from psalvato_appointment import *

class AppointmentBook(object):
    """Takes in a file of appointments of the form: Type|Day|Month|Year|Description.
    Creates a library of appointments sorted on their recurrence (daily/monthly/onetime)."""


    def __init__(self, file):
        self._file = open(file, 'r')
        self._book = {'D':[], 'O':[], 'M':[]}
        self._num = 1000

        #Reads the file and inputs correct type of appointment object into the dictionary.
        for line in self._file:
            line = line.rstrip().split('|')
            if line[0] == 'D':
                self._book['D'] += [Daily(line[1], line[2], line[3], line[4], self._num),]
                self._num += 1
            elif line[0] == 'M':
                self._book['M'] += [Monthly(line[1], line[2], line[3], line[4], self._num),]
                self._num += 1
            elif line[0] == 'O':
                self._book['O'] += [Onetime(line[1], line[2], line[3], line[4], self._num),]
                self._num += 1


    def addAppointment(self):
        """Allows the user to input the information for a new appointment and adds it to the library."""
        typeOf = input('\nD)ay, M)onth, or O)netime? ').upper()
        date = input('Date (day/month/year): ').split('/')
        if len(date) != 3:
            print('Invalid date input.\n')
            return None
        desc = input('Description: ')

        #Uses the type of appointment input to create an appointment object and add it to the appropriate dictionary key.
        if typeOf == 'D':
            self._book['D'] += [Daily(date[0], date[1], date[2], desc, self._num),]
            self._num += 1
        elif typeOf == 'M':
            self._book['M'] += [Monthly(date[0], date[1], date[2], desc, self._num),]
            self._num += 1
        elif typeOf == 'O':
            self._book['O'] += [Onetime(date[0], date[1], date[2], desc, self._num),]
            self._num += 1
        else:
            print('Invalid type input, "' + typeOf + '".')
        print()


    def saveApptBook(self, file):
        """Saves the appointments to a file in the order in which they were added."""
        allAppts = self.getAllAppts()
        toAppend = 'w'
        for appt in allAppts:
            appt.save(open(file, toAppend))
            toAppend = 'a'



    def listAppointments(self):
        """Prints all the appointments that occur on a specific date."""
        date = input('\nDate (day/month/year): ')
        if len(date.split('/')) != 3:
            print('Invalid date input.\n')
            return None
        print ('\nAppointments on: ' + date)

        #Checks each appointment and prints if the appointment will occur on the entered date.
        for appt in self._book['D']:
            if appt.occursOn(date.split('/')):
                print (appt.getDesc())
        for appt in self._book['O']:
            if appt.occursOn(date):
                print (appt.getDesc())
        for appt in self._book['M']:
            if appt.occursOn(date.split('/')):
                print(appt.getDesc())
        print()


    def getAllAppts(self):
        """Returns a list of all of the appointments,
        sorted based on when they were added to the appointment book."""
        allAppts = []
        for apptType in self._book:
            for appt in self._book[apptType]:
                allAppts += [appt,]
        allAppts.sort(key=Appointment.getApptNum)
        return allAppts


    def printApptBook(self):
        """Prints every appointment in the appointment book,
         sorted by when they were added, the first being first."""
        allAppts = self.getAllAppts()

        print()
        for appointment in allAppts:
            print(appointment)
        print()


    def deleteAppointment(self):
        """Allows the user to enter an appointment number.
        Removes the corresponding appointment from the appointment book."""
        num = input('\nAppointment number: ')
        for apptType in self._book:
            for appt in self._book[apptType]:
                if str(appt.getApptNum()) == str(num):
                    print ('Deleting: \n', appt)
                    self._book[apptType].pop(self._book[apptType].index(appt))
        print()


    def findAppointment(self):
        """Allows the user to enter the description of an appointment.
        Returns the type of appointment, date, and the description of
        any matching appointments in the appointment book."""
        found = False
        desc = input('\nDescription: ').lower()
        for apptType in self._book:
            for appt in self._book[apptType]:
                if str(appt.getDesc()).lower() == desc:
                    found = True
                    print (appt)
        if not found:
            print('The appointment "' + desc + '" does not exist.')
        print()

