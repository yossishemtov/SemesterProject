import random
from datetime import datetime, timedelta

# Park and Order Data
parks = [('Mount Rainier', 1), ('Yellow Stone', 2), ('Yosemite', 3)]
orderTypes = ['SOLO', 'FAMILY', 'GUIDEDGROUP']
basicStatuses = ['PENDING', 'CANCELED']  # Adjusting basic statuses
specialStatuses = ['NOTARRIVED', 'COMPLETED']  # Special statuses for dates before 1.4.2024
cutoff_date = datetime(2024, 4, 1)

# Helper function to generate a random datetime
def random_datetime(start, end):
    """Generate a random datetime between `start` and `end`."""
    return start + timedelta(
        seconds=random.randint(0, int((end - start).total_seconds())))

# Decide status, considering special rules
def decide_status(visit_datetime):
    now = datetime.now()
    if visit_datetime.date() < cutoff_date.date():
        if visit_datetime < now:
            return random.choice(specialStatuses+basicStatuses)
        else:
            # Before cutoff and in the future, could still be 'PENDING' or 'CONFIRMED'
            return random.choice(basicStatuses + ['CONFIRMED'])
    else:
        if visit_datetime> now:
            # Past the cutoff and the datetime has passed, 'CONFIRMED' is not an option
            return random.choice(basicStatuses)
        else:
            # Future visits after cutoff
            return random.choice(basicStatuses + ['CONFIRMED'])

# Generate initial orders
def generate_initial_orders(n):
    start_datetime = datetime(2024, 1, 1)
    end_datetime = datetime(2024, 12, 31, 23, 59, 59)
    for i in range(1, n + 1):
        orderId = i
        travelerId = random.randint(1000000, 9999999)
        parkName, parkNumber = random.choice(parks)
        orderType = random.choice(orderTypes)

        # Determine number of visitors based on order type
        if orderType == 'SOLO':
            visitors = 1
        elif orderType == 'FAMILY':
            visitors = random.randint(1, 10)
        elif orderType == 'GUIDEDGROUP':
            visitors = random.randint(1, 15)

        # Generate a random visit datetime within the year
        visit_datetime = random_datetime(start_datetime, end_datetime)

        # Decide on the status with the new rules
        status = decide_status(visit_datetime)

        price = visitors * 30.00
        email = f'traveler{orderId}@example.com'
        visit_date_str = visit_datetime.strftime('%Y-%m-%d')
        visit_time_str = visit_datetime.strftime('%H:%M:%S')
        phone = f'555-{random.randint(1000, 9999)}'

        print(
            f"({orderId}, {travelerId}, {parkNumber}, {visitors}, {price}, '{email}', '{visit_date_str}', '{phone}', '{visit_time_str}', '{status}', '{orderType}', '{parkName}'),")

# Generate initial orders
n = 1500 # Number of initial orders to generate
generate_initial_orders(n)


# Configuration
total_visits = 1500  # Adjust as needed
start_datetime = datetime(2024, 1, 1)
cutoff_datetime = datetime(2024, 4, 1)
end_datetime = datetime(2024, 3, 31, 23, 59, 59)  # Ensuring dates are before April 1, 2024
parks = [1, 2, 3]  # Park numbers

def random_datetime(start, end):
    """Generate a random datetime between `start` and `end`."""
    return start + timedelta(
        seconds=random.randint(0, int((end - start).total_seconds())))

def generate_visits(n):
    print("INSERT INTO `visit` (orderNumber, visitDate, enteringTime, exitingTime, parkNumber) VALUES")
    for i in range(1, n + 1):
        visitDate = random_datetime(start_datetime, end_datetime).strftime('%Y-%m-%d')
        enteringTime = random.randint(9, 15)  # Assuming park opens at 9 AM and last entry by 3 PM
        exitingTime = enteringTime + random.randint(1, 5)  # Assuming a visit lasts between 1 to 5 hours
        parkNumber = random.choice(parks)
        orderNumber = i  # Simulating orderNumber for simplicity, assuming each visit corresponds to a unique order

        # Formatting times
        enteringTime_str = f"{enteringTime}:00:00"  # Simplified to on the hour
        exitingTime_str = f"{exitingTime}:00:00" if exitingTime <= 17 else "17:00:00"  # No later than 5 PM

        # Handling the comma for the last INSERT statement
        comma = "," if i < n else ";"

        print(f"({orderNumber}, '{visitDate}', '{enteringTime_str}', '{exitingTime_str}', {parkNumber}){comma}")

generate_visits(total_visits)
