-- Zuno Database Schema Migration
-- Run this on Supabase SQL Editor (supabase.com → SQL Editor → New Query → Paste → Run)

-- Enable PostGIS for geospatial queries
CREATE EXTENSION IF NOT EXISTS postgis;

-- Enable pg_trgm for fuzzy text search
CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- ============================================
-- USERS TABLE
-- ============================================
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    phone TEXT UNIQUE NOT NULL,
    full_name TEXT,
    role TEXT NOT NULL CHECK (role IN ('SEEKER', 'OWNER')),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- ============================================
-- LISTINGS TABLE
-- ============================================
CREATE TABLE IF NOT EXISTS listings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    owner_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    pg_name TEXT NOT NULL,
    address TEXT NOT NULL,
    area TEXT NOT NULL,
    city TEXT NOT NULL DEFAULT 'Hyderabad',
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    gender_preference TEXT NOT NULL CHECK (gender_preference IN ('MALE', 'FEMALE', 'ANY')),
    monthly_rent INTEGER NOT NULL,
    security_deposit INTEGER,
    food_included BOOLEAN NOT NULL DEFAULT FALSE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    total_beds INTEGER NOT NULL DEFAULT 0,
    vacant_beds INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- Amenities stored as separate table (JPA @ElementCollection)
CREATE TABLE IF NOT EXISTS listing_amenities (
    listing_id UUID NOT NULL REFERENCES listings(id) ON DELETE CASCADE,
    amenity TEXT NOT NULL
);

-- House rules stored as separate table (JPA @ElementCollection)
CREATE TABLE IF NOT EXISTS listing_house_rules (
    listing_id UUID NOT NULL REFERENCES listings(id) ON DELETE CASCADE,
    rule TEXT NOT NULL
);

-- Photos stored as separate table (JPA @ElementCollection)
CREATE TABLE IF NOT EXISTS listing_photos (
    listing_id UUID NOT NULL REFERENCES listings(id) ON DELETE CASCADE,
    photo_url TEXT NOT NULL
);

-- ============================================
-- ROOMS TABLE
-- ============================================
CREATE TABLE IF NOT EXISTS rooms (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    listing_id UUID NOT NULL REFERENCES listings(id) ON DELETE CASCADE,
    room_label TEXT NOT NULL,
    room_type TEXT NOT NULL CHECK (room_type IN ('SINGLE', 'DOUBLE', 'TRIPLE')),
    capacity INTEGER NOT NULL,
    occupied_count INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- ============================================
-- TENANTS TABLE
-- ============================================
CREATE TABLE IF NOT EXISTS tenants (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    room_id UUID NOT NULL REFERENCES rooms(id) ON DELETE CASCADE,
    listing_id UUID NOT NULL REFERENCES listings(id) ON DELETE CASCADE,
    full_name TEXT NOT NULL,
    phone TEXT NOT NULL,
    aadhaar_front_url TEXT,
    aadhaar_back_url TEXT,
    move_in_date DATE NOT NULL,
    move_out_date DATE,
    status TEXT NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'MOVED_OUT')),
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- ============================================
-- SAVED LISTINGS TABLE
-- ============================================
CREATE TABLE IF NOT EXISTS saved_listings (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    listing_id UUID NOT NULL REFERENCES listings(id) ON DELETE CASCADE,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    UNIQUE(user_id, listing_id)
);

-- ============================================
-- LISTING VIEWS TABLE
-- ============================================
CREATE TABLE IF NOT EXISTS listing_views (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    listing_id UUID NOT NULL REFERENCES listings(id) ON DELETE CASCADE,
    viewer_id UUID REFERENCES users(id) ON DELETE SET NULL,
    viewed_at TIMESTAMPTZ DEFAULT NOW(),
    ip_address TEXT
);

-- ============================================
-- INDEXES (for fast queries)
-- ============================================
CREATE INDEX IF NOT EXISTS idx_listings_area ON listings(area);
CREATE INDEX IF NOT EXISTS idx_listings_gender ON listings(gender_preference);
CREATE INDEX IF NOT EXISTS idx_listings_rent ON listings(monthly_rent);
CREATE INDEX IF NOT EXISTS idx_listings_active ON listings(is_active);
CREATE INDEX IF NOT EXISTS idx_listings_owner ON listings(owner_id);
CREATE INDEX IF NOT EXISTS idx_rooms_listing ON rooms(listing_id);
CREATE INDEX IF NOT EXISTS idx_tenants_room ON tenants(room_id);
CREATE INDEX IF NOT EXISTS idx_tenants_listing ON tenants(listing_id);
CREATE INDEX IF NOT EXISTS idx_tenants_status ON tenants(status);
CREATE INDEX IF NOT EXISTS idx_saved_user ON saved_listings(user_id);
CREATE INDEX IF NOT EXISTS idx_views_listing ON listing_views(listing_id);
CREATE INDEX IF NOT EXISTS idx_views_date ON listing_views(viewed_at);

-- Area search index (for fuzzy/partial matching)
CREATE INDEX IF NOT EXISTS idx_listings_area_trgm ON listings USING gin (area gin_trgm_ops);

-- Geospatial index (for "PGs near me" queries)
CREATE INDEX IF NOT EXISTS idx_listings_location ON listings USING gist (
    ST_MakePoint(longitude, latitude)::geography
);

-- ============================================
-- FUNCTION: Auto-update updated_at timestamp
-- ============================================
CREATE OR REPLACE FUNCTION update_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER set_updated_at_users
    BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION update_updated_at();

CREATE TRIGGER set_updated_at_listings
    BEFORE UPDATE ON listings
    FOR EACH ROW EXECUTE FUNCTION update_updated_at();

-- ============================================
-- DONE! All tables, indexes, and triggers created.
-- ============================================
