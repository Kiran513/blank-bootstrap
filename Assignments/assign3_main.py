__author__ = 'paulsalvatore57'




from psalvato_Assign3 import AppointmentBook

def main() :

   mybook = AppointmentBook("data.txt")

   choice = input("A)dd, D)lete, L)ist, P)rint, F)ind, or Q)uit? ").upper()
   while choice != "Q" :
      if choice == "L" :
         mybook.listAppointments()
      if choice == "A" :
         mybook.addAppointment()
      if choice == "F" :
         mybook.findAppointment()
      if choice == "D" :
         mybook.deleteAppointment()
      if choice == "P" :
         mybook.printApptBook()

      choice = input("A)dd, D)lete, L)ist, P)rint, F)ind, or Q)uit? ").upper()

   # Save all of the appointments.
   print("Saving appointments to data.txt")
   mybook.saveApptBook("data.txt")

main()