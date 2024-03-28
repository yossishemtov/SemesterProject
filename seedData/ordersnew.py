import random
from datetime import datetime, timedelta, time

# Park and Order Data
parks = [('Mount Rainier', 1), ('Yellow Stone', 2), ('Yosemite', 3)]
orderTypes = ['SOLO', 'FAMILY', 'GUIDEDGROUP']
basicStatuses = ['PENDING', 'CANCELED']  # Adjusting basic statuses
specialStatuses = ['NOTARRIVED', 'COMPLETED']  # Special statuses for dates before 1.4.2024
cutoff_date = datetime(2024, 4, 1)

# Traveler IDs
traveler_ids = [
    123456789,
    123123123,
    123412341,
    123456788,
    123451234,
    987654321,
    121212121,
    111122223,
    232323232,
    123456777,
    234567890,
    234567891,
    234567892,
    234567893,
    234567894,
    234567895,
    234567896,
    234567897,
    234567898,
    234567899,
]

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
            return random.choice(specialStatuses + basicStatuses)
        else:
            return random.choice(basicStatuses + ['CONFIRMED'])
    else:
        if visit_datetime > now:
            return random.choice(basicStatuses)
        else:
            return random.choice(basicStatuses + ['CONFIRMED'])

# Generate initial orders
def generate_initial_orders(n):
    start_datetime = datetime(2024, 1, 1)
    end_datetime = datetime(2024, 12, 31, 23, 59, 59)
    for i in range(1, n + 1):
        orderId = i
        # Ensuring unique traveler ID for each order
        travelerId = random.choice(traveler_ids)  # Selecting a random traveler ID from the list
        parkName, parkNumber = random.choice(parks)
        orderType = random.choice(orderTypes)

        if orderType == 'SOLO':
            visitors = 1
        elif orderType == 'FAMILY':
            visitors = random.randint(1, 6)
        elif orderType == 'GUIDEDGROUP':
            visitors = random.randint(1, 15)


        # Generate a random visit date
        visit_date = random_datetime(start_datetime, end_datetime).date()

        # Generate a random visit time between 8 AM and 5:30 PM to allow for half-hour slots within operating hours
        hour = random.choice(range(8, 18))  # 8 AM to 5 PM for starting hour
        minute = random.choice([0, 30])  # Start at the hour or half-hour
        # Reconstruct the full datetime with the specified time
        visit_datetime = datetime.combine(visit_date, time(hour, minute))

        status = decide_status(visit_datetime)

        price = visitors * 30.00
        email = f'traveler{orderId}@example.com'
        visit_date_str = visit_datetime.strftime('%Y-%m-%d')
        visit_time_str = visit_datetime.strftime('%H:%M')
        phone = f'555-{random.randint(1000, 9999)}'

        print(
            f"({orderId}, {travelerId}, {parkNumber}, {visitors}, {price}, '{email}', '{visit_date_str}', '{phone}', '{visit_time_str}', '{status}', '{orderType}', '{parkName}'),")

# Number of initial orders to generate
n = 1500
generate_initial_orders(n)
