#! /usr/bin/env python
# coding=utf-8
import datetime
import sched
import time


class Scheduler:
    _schedule = None

    @staticmethod
    def _get_schedule_instance():
        if Scheduler._schedule is None:
            Scheduler._schedule = sched.scheduler(time.time, time.sleep)
        return Scheduler._schedule

    @staticmethod
    def _get_next_time(day=None, hour=None, mint=None, sec=None):
        time_now = datetime.datetime.now()
        run_time = datetime.datetime.now()

        if sec is None:
            run_time = time_now + datetime.timedelta(seconds=1)
        elif mint is None:
            run_time = time_now + datetime.timedelta(minutes=1)
        elif hour is None:
            run_time = time_now + datetime.timedelta(hours=1)
        elif day is None:
            run_time = time_now + datetime.timedelta(days=1)

        if sec is not None:
            run_time = run_time.replace(second=sec)
        if mint is not None:
            run_time = run_time.replace(minute=mint)
        if hour is not None:
            run_time = run_time.replace(hour=hour)
        if day is not None:
            run_time = run_time.replace(day=day)
        run_time = run_time.replace(microsecond=0)

        print("Scheduler_get_next_time", run_time)
        return run_time.timestamp()

    @staticmethod
    def _run_method(method, args=(), day=None, hour=None, mint=None, sec=None):
        method()
        Scheduler._get_schedule_instance().enterabs(Scheduler._get_next_time(day, hour, mint, sec), 0,
                                                    Scheduler._run_method, (method, args, day, hour, mint, sec))

    @staticmethod
    def add_method(method, args=(), day=None, hour=None, mint=None, sec=None):
        Scheduler._get_schedule_instance().enterabs(Scheduler._get_next_time(day, hour, mint, sec), 0,
                                                    Scheduler._run_method, (method, args, day, hour, mint, sec))

    @staticmethod
    def run():
        Scheduler._get_schedule_instance().run(False)
