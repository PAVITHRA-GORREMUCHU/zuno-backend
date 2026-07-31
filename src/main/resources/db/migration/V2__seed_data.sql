-- Zuno Seed Data — 5 sample PG listings for development/testing
-- Run this AFTER the schema migration

-- Create a sample owner user
INSERT INTO users (id, phone, full_name, role) VALUES
    ('11111111-1111-1111-1111-111111111111', '9876543210', 'Ramesh Kumar', 'OWNER');

-- Create 5 PG listings
INSERT INTO listings (id, owner_id, pg_name, address, area, city, latitude, longitude, gender_preference, monthly_rent, security_deposit, food_included, total_beds, vacant_beds) VALUES
    ('aaaa1111-1111-1111-1111-111111111111', '11111111-1111-1111-1111-111111111111', 'Sri Residency PG', 'Plot 45, Street No. 5', 'TNGOS Colony', 'Hyderabad', 17.4400, 78.3489, 'FEMALE', 8500, 20000, TRUE, 12, 3),
    ('aaaa2222-2222-2222-2222-222222222222', '11111111-1111-1111-1111-111111111111', 'Sai Krishna PG', '12-3-45, Main Road', 'Gachibowli', 'Hyderabad', 17.4401, 78.3567, 'MALE', 7000, 15000, FALSE, 8, 2),
    ('aaaa3333-3333-3333-3333-333333333333', '11111111-1111-1111-1111-111111111111', 'Green View PG', 'Flat 201, Madhura Nagar', 'Kondapur', 'Hyderabad', 17.4567, 78.3601, 'ANY', 9000, 18000, TRUE, 10, 4),
    ('aaaa4444-4444-4444-4444-444444444444', '11111111-1111-1111-1111-111111111111', 'Lakshmi Ladies PG', 'H.No 7-8, Phase 2', 'TNGOS Colony', 'Hyderabad', 17.4380, 78.3510, 'FEMALE', 10000, 25000, TRUE, 6, 1),
    ('aaaa5555-5555-5555-5555-555555555555', '11111111-1111-1111-1111-111111111111', 'Comfort Stay PG', '3rd Floor, Telecom Nagar', 'Gachibowli', 'Hyderabad', 17.4420, 78.3550, 'MALE', 12000, 20000, FALSE, 4, 2);

-- Add amenities
INSERT INTO listing_amenities (listing_id, amenity) VALUES
    ('aaaa1111-1111-1111-1111-111111111111', 'WiFi'),
    ('aaaa1111-1111-1111-1111-111111111111', 'AC'),
    ('aaaa1111-1111-1111-1111-111111111111', 'Food'),
    ('aaaa1111-1111-1111-1111-111111111111', 'Laundry'),
    ('aaaa1111-1111-1111-1111-111111111111', 'Security'),
    ('aaaa2222-2222-2222-2222-222222222222', 'WiFi'),
    ('aaaa2222-2222-2222-2222-222222222222', 'Parking'),
    ('aaaa3333-3333-3333-3333-333333333333', 'WiFi'),
    ('aaaa3333-3333-3333-3333-333333333333', 'AC'),
    ('aaaa3333-3333-3333-3333-333333333333', 'Food'),
    ('aaaa3333-3333-3333-3333-333333333333', 'Laundry'),
    ('aaaa4444-4444-4444-4444-444444444444', 'WiFi'),
    ('aaaa4444-4444-4444-4444-444444444444', 'AC'),
    ('aaaa4444-4444-4444-4444-444444444444', 'Food'),
    ('aaaa4444-4444-4444-4444-444444444444', 'Security'),
    ('aaaa4444-4444-4444-4444-444444444444', 'Attached Bathroom'),
    ('aaaa5555-5555-5555-5555-555555555555', 'WiFi'),
    ('aaaa5555-5555-5555-5555-555555555555', 'Laundry');

-- Add house rules
INSERT INTO listing_house_rules (listing_id, rule) VALUES
    ('aaaa1111-1111-1111-1111-111111111111', 'Gate closes at 10:30 PM'),
    ('aaaa1111-1111-1111-1111-111111111111', 'No smoking'),
    ('aaaa1111-1111-1111-1111-111111111111', 'Female residents only'),
    ('aaaa2222-2222-2222-2222-222222222222', 'No smoking'),
    ('aaaa2222-2222-2222-2222-222222222222', 'No alcohol'),
    ('aaaa3333-3333-3333-3333-333333333333', 'Gate closes at 11:00 PM'),
    ('aaaa4444-4444-4444-4444-444444444444', 'Gate closes at 10:00 PM'),
    ('aaaa4444-4444-4444-4444-444444444444', 'No male visitors'),
    ('aaaa5555-5555-5555-5555-555555555555', 'No smoking');

-- Add rooms
INSERT INTO rooms (id, listing_id, room_label, room_type, capacity, occupied_count) VALUES
    -- Sri Residency (12 beds, 3 vacant)
    ('bbbb1111-0001-0000-0000-000000000000', 'aaaa1111-1111-1111-1111-111111111111', 'Room 101', 'DOUBLE', 2, 2),
    ('bbbb1111-0002-0000-0000-000000000000', 'aaaa1111-1111-1111-1111-111111111111', 'Room 102', 'DOUBLE', 2, 1),
    ('bbbb1111-0003-0000-0000-000000000000', 'aaaa1111-1111-1111-1111-111111111111', 'Room 103', 'TRIPLE', 3, 2),
    ('bbbb1111-0004-0000-0000-000000000000', 'aaaa1111-1111-1111-1111-111111111111', 'Room 104', 'SINGLE', 1, 0),
    ('bbbb1111-0005-0000-0000-000000000000', 'aaaa1111-1111-1111-1111-111111111111', 'Room 105', 'DOUBLE', 2, 2),
    ('bbbb1111-0006-0000-0000-000000000000', 'aaaa1111-1111-1111-1111-111111111111', 'Room 106', 'DOUBLE', 2, 2),
    -- Sai Krishna (8 beds, 2 vacant)
    ('bbbb2222-0001-0000-0000-000000000000', 'aaaa2222-2222-2222-2222-222222222222', 'Room A1', 'DOUBLE', 2, 2),
    ('bbbb2222-0002-0000-0000-000000000000', 'aaaa2222-2222-2222-2222-222222222222', 'Room A2', 'DOUBLE', 2, 1),
    ('bbbb2222-0003-0000-0000-000000000000', 'aaaa2222-2222-2222-2222-222222222222', 'Room A3', 'DOUBLE', 2, 2),
    ('bbbb2222-0004-0000-0000-000000000000', 'aaaa2222-2222-2222-2222-222222222222', 'Room A4', 'DOUBLE', 2, 1),
    -- Green View (10 beds, 4 vacant)
    ('bbbb3333-0001-0000-0000-000000000000', 'aaaa3333-3333-3333-3333-333333333333', 'Room 1', 'TRIPLE', 3, 2),
    ('bbbb3333-0002-0000-0000-000000000000', 'aaaa3333-3333-3333-3333-333333333333', 'Room 2', 'TRIPLE', 3, 3),
    ('bbbb3333-0003-0000-0000-000000000000', 'aaaa3333-3333-3333-3333-333333333333', 'Room 3', 'DOUBLE', 2, 0),
    ('bbbb3333-0004-0000-0000-000000000000', 'aaaa3333-3333-3333-3333-333333333333', 'Room 4', 'DOUBLE', 2, 1);

-- Done! 5 listings, amenities, rules, and rooms with realistic occupancy.
