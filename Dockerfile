# start from an official image
FROM python:3.6

# arbitrary location choice: you can change the directory
RUN mkdir -p /opt/services/djangoapp/src
WORKDIR /opt/services/djangoapp/src
# copy our project code
COPY . /opt/services/djangoapp/src
RUN ls -l /opt/services/djangoapp/src
RUN pip3 install -r /opt/services/djangoapp/src/requirements.txt
RUN pip3 install mysqlclient
# expose the port 8000
EXPOSE 8002

# define the default command to run when starting the container
CMD ["gunicorn", "--chdir", "fundoo", "--bind", ":8002", "fundoo.wsgi:application"]

