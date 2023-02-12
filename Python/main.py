import mysql.connector
import pymongo


print("Die Migration kann mehrere Minuten dauern")

# Zuerst werden die Ergebnisse von der alten Datenbank geladen und zwischengespeichert
mysqldb = mysql.connector.connect(host="localhost", database="scenario_spotify", user="nando", password="nando")
user = mysqldb.cursor(dictionary=True);
user.execute("SELECT * from user ;");
user_result = user.fetchall();
print("User Abfrage beendet")

playlist = mysqldb.cursor(dictionary=True);
playlist.execute("SELECT * from playlist;");
playlist_result = playlist.fetchall();
print("Playlist Abfrage beendet")

song = mysqldb.cursor(dictionary=True);
song.execute("SELECT * from song;");
song_result = song.fetchall();
print("Song Abfrage beendet")

playlist_follower = mysqldb.cursor(dictionary=True);
playlist_follower.execute("SELECT * from  playlist join playlist_follower on playlist.id = playlist_follower.playlist_id;");
playlist_follower_result = playlist_follower.fetchall();
print("Playlist_follower Abfrage beendet")

playlist_song = mysqldb.cursor(dictionary=True);
playlist_song.execute("SELECT song.id AS song__id,song.title AS song_title ,song.artist AS song_artist,playlist_song.position AS playlist_song_position, playlist_song.playlist_id AS playlist_song_playlist_id from playlist join playlist_song on playlist_song.playlist_id = playlist.id join song on playlist_song.song_id = song.id; ");
playlist_song_result = playlist_song.fetchall();
print("Playlist_song Abfrage beendet")




# Verbindung zu MongoDB
mongodb_host = "mongodb://localhost:27017/"
mongodb_dbname = "Spotify_2"
myclient = pymongo.MongoClient(mongodb_host)
mydb = myclient[mongodb_dbname]
usercol = mydb["user"]
playlistcol = mydb["playlist"]
songcol = mydb["song"]


# Ergebnisse der Playlist-Abfrage werden in einer Collection abgespeichert in MongoDB
for row in playlist_result:
    playlistcol.insert_one(
        {"id": row["id"], "name": row["name"], "created_date": row["created_date"], "owner_id": row["owner_id"]})
playlistcol.create_index([("id", 1)])
print("Playlist Collection steht")


# Ergebnisse der Playlist-Follower werden in der Playlist-Collection abgespeichert
for row in playlist_follower_result:
    follower = {
        "follower_id": row["follower_id"],
    }
    playlistcol.update_one(
        {"id": row["playlist_id"]},
        {"$push": {"followers": follower}},
    )
playlistcol.create_index([("owner_id", 1)])
playlistcol.create_index([("followers.follower_id", 1)])
print("playlist_follower upgedatet")


# Ergebnisse der User-Abfrage werden in einer User-Collection abgespeichert und in Playlist werden die alten ids
# durch neue Ersetzt
for row in user_result:
    user = usercol.insert_one({"name": row["name"], "gender": row["gender"]})
    playlistcol.update_many(
        {"owner_id": row["id"]},
        {"$set": {"owner_id": user.inserted_id}},
        # upsert=True,
    )
    playlistcol.update_many(
        {"followers.follower_id": row["id"]},
        { "$set": { "followers.$.follower_id" : user.inserted_id } },
        # upsert=True,
    )
print("User Collection steht und owner_id,follower_id upgedatet")


# In Playlist werden nun die Songs eingesetzt mit ihrer ID und Position
for row in playlist_song_result:
    song = {
        "song_id": row["song__id"],
        "position": row["playlist_song_position"],
    }
    playlistcol.update_one(
        {"id": row["playlist_song_playlist_id"]},
        {"$push": {"songs": song}},
    )
playlistcol.create_index([("songs.song_id", 1)])
print("Playlist_song upgedatet")


# Die Collection Songs wird befüllt
for row in song_result:
    song = songcol.insert_one({"artist": row["artist"], "title": row["title"]})
    playlistcol.update_many(
        {"songs.song_id": row["id"]},
        { "$set": { "songs.$.song_id" : song.inserted_id } },
        # upsert=True,
    )
print("Song Collection steht und song_id upgedatet")


# Die alte ID in Playlist wird gelöscht
playlistcol.update_many({}, {'$unset': {'id':1}})
print("Playlist id geloescht")


print("Migration beendet")