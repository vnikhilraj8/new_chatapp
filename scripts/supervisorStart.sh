sudo cp /demo/fundoo/fundoo/settings.py /new_chatapp/fundoo/fundoo/settings.py
sudo supervisorctl stop all
sudo supervisorctl start all
sudo supervisorctl update